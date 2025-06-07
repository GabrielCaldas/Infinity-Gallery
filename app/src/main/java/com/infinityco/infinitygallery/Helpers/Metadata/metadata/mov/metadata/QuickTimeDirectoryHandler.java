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

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.quicktime.QuickTimeHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeAtomTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeContainerTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeMetadataHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.Atom;

import java.io.IOException;

/**
 * @author Payton Garland
 */
public class QuickTimeDirectoryHandler extends QuickTimeMetadataHandler
{
    private String currentData;

    public QuickTimeDirectoryHandler(Metadata metadata)
    {
        super(metadata);
    }

    @Override
    protected boolean shouldAcceptAtom(@NotNull Atom atom)
    {
        return atom.type.equals(QuickTimeAtomTypes.ATOM_DATA);
    }

    @Override
    protected boolean shouldAcceptContainer(@NotNull Atom atom)
    {
        return QuickTimeMetadataDirectory._tagIntegerMap.containsKey(atom.type)
            || atom.type.equals(QuickTimeContainerTypes.ATOM_METADATA_LIST);
    }

    @Override
    protected QuickTimeHandler processAtom(@NotNull Atom atom, @Nullable byte[] payload) throws IOException
    {
        if (payload != null) {
            SequentialByteArrayReader reader = new SequentialByteArrayReader(payload);
            if (atom.type.equals(QuickTimeAtomTypes.ATOM_DATA) && currentData != null) {
                processData(payload, reader);
            } else {
                currentData = new String(reader.getBytes(4));
            }
        } else {
            if (QuickTimeMetadataDirectory._tagIntegerMap.containsKey(atom.type)) {
                currentData = atom.type;
            } else {
                currentData = null;
            }
        }
        return this;
    }

    @Override
    protected void processData(@NotNull byte[] payload, @NotNull SequentialByteArrayReader reader) throws IOException
    {
        // 4 bytes: type indicator
        // 4 bytes: locale indicator
        reader.skip(8);
        String value = new String(reader.getBytes(payload.length - 8));
        directory.setString(QuickTimeMetadataDirectory._tagIntegerMap.get(currentData), value);
    }

    @Override
    protected void processKeys(@NotNull SequentialByteArrayReader reader) throws IOException
    {
        // Do nothing
    }
}
