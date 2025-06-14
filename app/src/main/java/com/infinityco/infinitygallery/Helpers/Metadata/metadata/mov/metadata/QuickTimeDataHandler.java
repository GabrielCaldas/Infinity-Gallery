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
import com.infinityco.infinitygallery.Helpers.Metadata.lang.ByteUtil;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeAtomTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeContainerTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeMetadataHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.Atom;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Payton Garland
 */
public class QuickTimeDataHandler extends QuickTimeMetadataHandler
{
    private int currentIndex = 0;
    private ArrayList<String> keys = new ArrayList<String>();

    public QuickTimeDataHandler(Metadata metadata)
    {
        super(metadata);
    }

    @Override
    protected boolean shouldAcceptAtom(@NotNull Atom atom)
    {
        return atom.type.equals(QuickTimeAtomTypes.ATOM_HANDLER)
            || atom.type.equals(QuickTimeAtomTypes.ATOM_KEYS)
            || atom.type.equals(QuickTimeAtomTypes.ATOM_DATA);
    }

    @Override
    protected boolean shouldAcceptContainer(@NotNull Atom atom)
    {
        return atom.type.equals(QuickTimeContainerTypes.ATOM_METADATA_LIST)
            || ByteUtil.getInt32(atom.type.getBytes(), 0, true) <= keys.size();
    }

    @Override
    protected QuickTimeHandler processAtom(@NotNull Atom atom, @Nullable byte[] payload) throws IOException
    {
        if (payload != null) {
            SequentialByteArrayReader reader = new SequentialByteArrayReader(payload);
            if (atom.type.equals(QuickTimeAtomTypes.ATOM_KEYS)) {
                processKeys(reader);
            } else if (atom.type.equals(QuickTimeAtomTypes.ATOM_DATA)) {
                processData(payload, reader);
            }
        } else {
            int numValue = ByteUtil.getInt32(atom.type.getBytes(), 0, true);
            if (numValue > 0 && numValue < keys.size() + 1) {
                currentIndex = numValue - 1;
            }
        }
        return this;
    }

    @Override
    protected void processKeys(@NotNull SequentialByteArrayReader reader) throws IOException
    {
        // Version 1-byte and Flags 3-bytes
        reader.skip(4);
        int entryCount = reader.getInt32();
        for (int i = 0; i < entryCount; i++) {
            int keySize = reader.getInt32();
            reader.skip(4); // key namespace
            String keyValue = new String(reader.getBytes(keySize - 8));
            keys.add(keyValue);
        }
    }

    @Override
    protected void processData(@NotNull byte[] payload, @NotNull SequentialByteArrayReader reader) throws IOException
    {
        int type = reader.getInt32();
        // 4 bytes: locale indicator
        reader.skip(4);
        Integer tag = QuickTimeMetadataDirectory._tagIntegerMap.get(keys.get(currentIndex));
        if (tag != null) {
            int length = payload.length - 8;
            switch (type) {
                case 1:
                    directory.setString(tag, reader.getString(length, "UTF-8"));
                    break;
                case 13:
                case 14:
                case 27:
                    directory.setByteArray(tag, reader.getBytes(length));
                    break;
                case 22:
                    byte[] buf = new byte[4];
                    reader.getBytes(buf, 4 - length, length);
                    directory.setInt(tag, new SequentialByteArrayReader(buf).getInt32());
                    break;
                case 23:
                    directory.setFloat(tag, reader.getFloat32());
                    break;
                case 30:
                    int[] value = new int[length / 4];
                    for (int i = 0; i < value.length; i++) {
                        value[i] = reader.getInt32();
                    }
                    directory.setIntArray(tag, value);
                    break;
            }
        }
    }
}
