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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap2/qtff2.html#//apple_ref/doc/uid/TP40000939-CH204-25691
 *
 * @author Payton Garland
 */
public abstract class SampleDescriptionAtom<T extends SampleDescription> extends FullAtom
{
    long numberOfEntries;
    ArrayList<T> sampleDescriptions;

    public SampleDescriptionAtom(SequentialReader reader, Atom atom) throws IOException
    {
        super(reader, atom);

        numberOfEntries = reader.getUInt32();
        sampleDescriptions = new ArrayList<T>((int)numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            sampleDescriptions.add(getSampleDescription(reader));
        }
    }

    @Nullable
    abstract T getSampleDescription(SequentialReader reader) throws IOException;
}
