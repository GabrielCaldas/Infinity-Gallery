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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.media;

import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeDescriptor;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeDirectory;

/**
 * @author Payton Garland
 */
public class QuickTimeSubtitleDescriptor extends QuickTimeDescriptor
{
    public QuickTimeSubtitleDescriptor(QuickTimeDirectory directory)
    {
        super(directory);
    }
}
