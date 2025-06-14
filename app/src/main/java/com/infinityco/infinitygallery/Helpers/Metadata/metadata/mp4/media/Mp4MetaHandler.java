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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.media;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.Mp4ContainerTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.Mp4MediaHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.boxes.Box;

import java.io.IOException;

public class Mp4MetaHandler extends Mp4MediaHandler<Mp4MetaDirectory>
{
    public Mp4MetaHandler(Metadata metadata)
    {
        super(metadata);
    }

    @NotNull
    @Override
    protected Mp4MetaDirectory getDirectory()
    {
        return directory;
    }

    @Override
    protected String getMediaInformation()
    {
        return Mp4ContainerTypes.BOX_MEDIA_NULL;
    }

    @Override
    protected void processSampleDescription(@NotNull SequentialReader reader, @NotNull Box box) throws IOException
    {

    }

    @Override
    protected void processMediaInformation(@NotNull SequentialReader reader, @NotNull Box box) throws IOException
    {

    }

    @Override
    protected void processTimeToSample(@NotNull SequentialReader reader, @NotNull Box box) throws IOException
    {

    }
}
