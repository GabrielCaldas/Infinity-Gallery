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
package com.infinityco.infinitygallery.Helpers.Metadata.imaging.heif;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.heif.HeifDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.heif.boxes.Box;

import java.io.IOException;

public abstract class HeifHandler<T extends HeifDirectory>
{
    protected Metadata metadata;
    protected T directory;

    public HeifHandler(Metadata metadata)
    {
        this.metadata = metadata;
        this.directory = getDirectory();
        metadata.addDirectory(directory);
    }

    protected abstract T getDirectory();

    protected abstract boolean shouldAcceptBox(@NotNull Box box);

    protected abstract boolean shouldAcceptContainer(@NotNull Box box);

    protected abstract HeifHandler processBox(@NotNull Box box, @NotNull byte[] payload) throws IOException;

    /**
     * There is potential for a box to both contain other boxes and contain information, so this method will
     * handle those occurences.
     */
    protected abstract void processContainer(@NotNull Box box, @NotNull SequentialReader reader) throws IOException;
}
