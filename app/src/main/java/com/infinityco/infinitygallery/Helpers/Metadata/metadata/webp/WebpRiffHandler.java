/*
 * Copyright 2002-2017 Drew Noakes
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com.infinityco.infinitygallery.Helpers.Metadatanoakes/metadata-extractor
 */
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.webp;

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.riff.RiffHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.ByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.RandomAccessReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.exif.ExifReader;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.icc.IccReader;

import java.io.IOException;

/**
 * Implementation of {@link RiffHandler} specialising in WebP support.
 *
 * Extracts data from chunk types:
 *
 * <ul>
 *     <li><code>"VP8X"</code>: width, height, is animation, has alpha</li>
 *     <li><code>"EXIF"</code>: full Exif data</li>
 *     <li><code>"ICCP"</code>: full ICC profile</li>
 *     <li><code>"XMP "</code>: full XMP data</li>
 * </ul>
 */
public class WebpRiffHandler implements RiffHandler
{
    @NotNull
    private final Metadata _metadata;

    public WebpRiffHandler(@NotNull Metadata metadata)
    {
        _metadata = metadata;
    }

    public boolean shouldAcceptRiffIdentifier(@NotNull String identifier)
    {
        return identifier.equals(WebpDirectory.FORMAT);
    }

    public boolean shouldAcceptChunk(@NotNull String fourCC)
    {
        return fourCC.equals(WebpDirectory.CHUNK_VP8X)
            || fourCC.equals(WebpDirectory.CHUNK_VP8L)
            || fourCC.equals(WebpDirectory.CHUNK_VP8)
            || fourCC.equals(WebpDirectory.CHUNK_EXIF)
            || fourCC.equals(WebpDirectory.CHUNK_ICCP)
            || fourCC.equals(WebpDirectory.CHUNK_XMP);
    }

    @Override
    public boolean shouldAcceptList(@NotNull String fourCC)
    {
        return false;
    }

    public void processChunk(@NotNull String fourCC, @NotNull byte[] payload)
    {
//        System.out.println("Chunk " + fourCC + " " + payload.length + " bytes");
        WebpDirectory directory = new WebpDirectory();
        if (fourCC.equals(WebpDirectory.CHUNK_EXIF)) {
            new ExifReader().extract(new ByteArrayReader(payload), _metadata);
        } else if (fourCC.equals(WebpDirectory.CHUNK_ICCP)) {
            new IccReader().extract(new ByteArrayReader(payload), _metadata);
        } else if (fourCC.equals(WebpDirectory.CHUNK_VP8X) && payload.length == 10) {
            RandomAccessReader reader = new ByteArrayReader(payload);
            reader.setMotorolaByteOrder(false);

            try {
                // Flags
//                boolean hasFragments = reader.getBit(0);
                boolean isAnimation = reader.getBit(1);
//                boolean hasXmp = reader.getBit(2);
//                boolean hasExif = reader.getBit(3);
                boolean hasAlpha = reader.getBit(4);
//                boolean hasIcc = reader.getBit(5);

                // Image size
                int widthMinusOne = reader.getInt24(4);
                int heightMinusOne = reader.getInt24(7);

                directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, widthMinusOne + 1);
                directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, heightMinusOne + 1);
                directory.setBoolean(WebpDirectory.TAG_HAS_ALPHA, hasAlpha);
                directory.setBoolean(WebpDirectory.TAG_IS_ANIMATION, isAnimation);

                _metadata.addDirectory(directory);

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        } else if (fourCC.equals(WebpDirectory.CHUNK_VP8L) && payload.length > 4) {
            RandomAccessReader reader = new ByteArrayReader(payload);
            reader.setMotorolaByteOrder(false);

            try {
                // https://developers.google.com/speed/webp/docs/webp_lossless_bitstream_specification#2_riff_header

                // Expect the signature byte
                if (reader.getInt8(0) != 0x2F)
                    return;
                int b1 = reader.getUInt8(1);
                int b2 = reader.getUInt8(2);
                int b3 = reader.getUInt8(3);
                int b4 = reader.getUInt8(4);
                // 14 bits for width
                int widthMinusOne = (b2 & 0x3F) << 8 | b1;
                // 14 bits for height
                int heightMinusOne = (b4 & 0x0F) << 10 | b3 << 2 | (b2 & 0xC0) >> 6;

                directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, widthMinusOne + 1);
                directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, heightMinusOne + 1);

                _metadata.addDirectory(directory);

            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        } else if (fourCC.equals(WebpDirectory.CHUNK_VP8) && payload.length > 9) {
            RandomAccessReader reader = new ByteArrayReader(payload);
            reader.setMotorolaByteOrder(false);

            try {
                // https://tools.ietf.org/html/rfc6386#section-9.1
                // https://github.com/webmproject/libwebp/blob/master/src/enc/syntax.c#L115

                // Expect the signature bytes
                if (reader.getUInt8(3) != 0x9D ||
                    reader.getUInt8(4) != 0x01 ||
                    reader.getUInt8(5) != 0x2A)
                    return;
                int width = reader.getUInt16(6);
                int height = reader.getUInt16(8);

                directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, width);
                directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, height);

                _metadata.addDirectory(directory);

            } catch (IOException ex) {
                directory.addError(ex.getMessage());
            }
        }
    }
}
