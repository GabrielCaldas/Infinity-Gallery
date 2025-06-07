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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov;

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.quicktime.QuickTimeHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.*;

import java.io.IOException;

/**
 * @author Payton Garland
 */
public class QuickTimeAtomHandler extends QuickTimeHandler<QuickTimeDirectory>
{
    private QuickTimeHandlerFactory handlerFactory = new QuickTimeHandlerFactory(this);

    public QuickTimeAtomHandler(Metadata metadata)
    {
        super(metadata);
    }

    @NotNull
    @Override
    protected QuickTimeDirectory getDirectory()
    {
        return new QuickTimeDirectory();
    }

    @Override
    public boolean shouldAcceptAtom(@NotNull Atom atom)
    {
        return atom.type.equals(QuickTimeAtomTypes.ATOM_FILE_TYPE)
            || atom.type.equals(QuickTimeAtomTypes.ATOM_MOVIE_HEADER)
            || atom.type.equals(QuickTimeAtomTypes.ATOM_HANDLER)
            || atom.type.equals(QuickTimeAtomTypes.ATOM_MEDIA_HEADER);
    }

    @Override
    public boolean shouldAcceptContainer(@NotNull Atom atom)
    {
        return atom.type.equals(QuickTimeContainerTypes.ATOM_TRACK)
            || atom.type.equals(QuickTimeContainerTypes.ATOM_USER_DATA)
            || atom.type.equals(QuickTimeContainerTypes.ATOM_METADATA)
            || atom.type.equals(QuickTimeContainerTypes.ATOM_MOVIE)
            || atom.type.equals(QuickTimeContainerTypes.ATOM_MEDIA);
    }

    @Override
    public QuickTimeHandler processAtom(@NotNull Atom atom, @Nullable byte[] payload) throws IOException
    {
        if (payload != null) {
            SequentialReader reader = new SequentialByteArrayReader(payload);

            if (atom.type.equals(QuickTimeAtomTypes.ATOM_MOVIE_HEADER)) {
                MovieHeaderAtom movieHeaderAtom = new MovieHeaderAtom(reader, atom);
                movieHeaderAtom.addMetadata(directory);
            } else if (atom.type.equals(QuickTimeAtomTypes.ATOM_FILE_TYPE)) {
                FileTypeCompatibilityAtom fileTypeCompatibilityAtom = new FileTypeCompatibilityAtom(reader, atom);
                fileTypeCompatibilityAtom.addMetadata(directory);
            } else if (atom.type.equals(QuickTimeAtomTypes.ATOM_HANDLER)) {
                HandlerReferenceAtom handlerReferenceAtom = new HandlerReferenceAtom(reader, atom);
                return handlerFactory.getHandler(handlerReferenceAtom.getComponentType(), metadata);
            } else if (atom.type.equals(QuickTimeAtomTypes.ATOM_MEDIA_HEADER)) {
                MediaHeaderAtom mediaHeaderAtom = new MediaHeaderAtom(reader, atom);
            }
        } else {
            if (atom.type.equals(QuickTimeContainerTypes.ATOM_COMPRESSED_MOVIE)) {
                directory.addError("Compressed QuickTime movies not supported");
            }
        }
        return this;
    }
}
