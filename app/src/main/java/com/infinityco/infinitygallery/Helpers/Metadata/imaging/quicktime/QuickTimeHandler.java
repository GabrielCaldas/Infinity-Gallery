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
package com.infinityco.infinitygallery.Helpers.Metadata.imaging.quicktime;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.Atom;

import java.io.IOException;

/**
 * @author Payton Garland
 */
public abstract class QuickTimeHandler<T extends QuickTimeDirectory>
{
    @NotNull protected Metadata metadata;
    @NotNull protected T directory;

    public QuickTimeHandler(@NotNull Metadata metadata)
    {
        this.metadata = metadata;
        this.directory = getDirectory();
        metadata.addDirectory(directory);
    }

    @NotNull
    protected abstract T getDirectory();

    protected abstract boolean shouldAcceptAtom(@NotNull Atom atom);

    protected abstract boolean shouldAcceptContainer(@NotNull Atom atom);

    protected abstract QuickTimeHandler processAtom(@NotNull Atom atom, @Nullable byte[] payload) throws IOException;

    protected QuickTimeHandler processContainer(@NotNull Atom atom) throws IOException
    {
        return processAtom(atom, null);
    }

    public void addError(@NotNull String message)
    {
        directory.addError(message);
    }
}
