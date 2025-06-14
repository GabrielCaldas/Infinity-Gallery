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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.jpeg;

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.jpeg.JpegSegmentMetadataReader;
import com.infinityco.infinitygallery.Helpers.Metadata.imaging.jpeg.JpegSegmentType;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.StringValue;

import java.util.Collections;

/**
 * Decodes the comment stored within JPEG files, populating a {@link Metadata} object with tag values in a
 * {@link JpegCommentDirectory}.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
public class JpegCommentReader implements JpegSegmentMetadataReader
{
    @NotNull
    public Iterable<JpegSegmentType> getSegmentTypes()
    {
        return Collections.singletonList(JpegSegmentType.COM);
    }

    public void readJpegSegments(@NotNull Iterable<byte[]> segments, @NotNull Metadata metadata, @NotNull JpegSegmentType segmentType)
    {
        for (byte[] segmentBytes : segments) {
            JpegCommentDirectory directory = new JpegCommentDirectory();
            metadata.addDirectory(directory);

            // The entire contents of the directory are the comment
            directory.setStringValue(JpegCommentDirectory.TAG_COMMENT, new StringValue(segmentBytes, null));
        }
    }
}
