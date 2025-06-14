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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.jfxx;

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.jpeg.JpegSegmentMetadataReader;
import com.infinityco.infinitygallery.Helpers.Metadata.imaging.jpeg.JpegSegmentType;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.ByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.RandomAccessReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.MetadataReader;

import java.io.IOException;
import java.util.Collections;

/**
 * Reader for JFXX (JFIF extensions) data, found in the APP0 JPEG segment.
 *
 * <ul>
 *   <li>http://en.wikipedia.org/wiki/JPEG_File_Interchange_Format</li>
 *   <li>http://www.w3.org/Graphics/JPEG/jfif3.pdf</li>
 * </ul>
 *
 * @author Drew Noakes
 */
public class JfxxReader implements JpegSegmentMetadataReader, MetadataReader
{
    public static final String PREAMBLE = "JFXX";

    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes()
    {
        return Collections.singletonList(JpegSegmentType.APP0);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType)
    {
        for (byte[] segmentBytes : segments) {
            // Skip segments not starting with the required header
            if (segmentBytes.length >= PREAMBLE.length() && PREAMBLE.equals(new String(segmentBytes, 0, PREAMBLE.length())))
                extract(new ByteArrayReader(segmentBytes), metadata);
        }
    }

    /**
     * Performs the JFXX data extraction, adding found values to the specified
     * instance of {@link Metadata}.
     */
    public void extract(@NotNull final RandomAccessReader reader, @NotNull final Metadata metadata)
    {
        JfxxDirectory directory = new JfxxDirectory();
        metadata.addDirectory(directory);

        try {
            // For JFXX, the tag number is also the offset into the segment

            directory.setInt(JfxxDirectory.TAG_EXTENSION_CODE, reader.getUInt8(JfxxDirectory.TAG_EXTENSION_CODE));
        } catch (IOException me) {
            directory.addError(me.getMessage());
        }
    }
}
