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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.exif.makernotes;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.TagDescriptor;

/**
 * Provides human-readable string representations of tag values stored in a {@link AppleMakernoteDirectory}.
 * <p>
 * Using information from http://owl.phy.queensu.ca/~phil/exiftool/TagNames/Apple.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
@SuppressWarnings("WeakerAccess")
public class AppleMakernoteDescriptor extends TagDescriptor<AppleMakernoteDirectory>
{
    public AppleMakernoteDescriptor(@NotNull AppleMakernoteDirectory directory)
    {
        super(directory);
    }

    @Override
    @Nullable
    public String getDescription(int tagType)
    {
        switch (tagType) {
            case AppleMakernoteDirectory.TAG_HDR_IMAGE_TYPE:
                return getHdrImageTypeDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    @Nullable
    public String getHdrImageTypeDescription()
    {
        return getIndexedDescription(AppleMakernoteDirectory.TAG_HDR_IMAGE_TYPE, 3, "HDR Image", "Original Image");
    }
}
