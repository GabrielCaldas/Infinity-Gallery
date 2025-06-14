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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.metadata;

import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeDescriptor;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeDirectory;

import static com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.metadata.QuickTimeMetadataDirectory.*;

/**
 * @author Payton Garland
 */
public class QuickTimeMetadataDescriptor extends QuickTimeDescriptor
{
    public QuickTimeMetadataDescriptor(QuickTimeDirectory directory)
    {
        super(directory);
    }

    @Override
    public String getDescription(int tagType)
    {
        switch (tagType) {
            case TAG_ARTWORK:
                return getArtworkDescription();
            case TAG_LOCATION_ROLE:
                return getLocationRoleDescription();
            default:
                return super.getDescription(tagType);
        }
    }

    private String getArtworkDescription()
    {
        return getByteLengthDescription(TAG_ARTWORK);
    }

    private String getLocationRoleDescription()
    {
        return getIndexedDescription(TAG_LOCATION_ROLE, 0,
            "Shooting location",
            "Real location",
            "Fictional location"
        );
    }
}
