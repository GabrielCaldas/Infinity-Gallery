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

import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeAtomTypes;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.QuickTimeMediaHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.Atom;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.TimecodeInformationMediaAtom;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.TimecodeSampleDescriptionAtom;

import java.io.IOException;

/**
 * @author Payton Garland
 */
public class QuickTimeTimecodeHandler extends QuickTimeMediaHandler<QuickTimeTimecodeDirectory>
{
    public QuickTimeTimecodeHandler(Metadata metadata)
    {
        super(metadata);
    }

    @NotNull
    @Override
    protected QuickTimeTimecodeDirectory getDirectory()
    {
        return new QuickTimeTimecodeDirectory();
    }

    @Override
    protected String getMediaInformation()
    {
        return QuickTimeAtomTypes.ATOM_TIMECODE_MEDIA_INFO;
    }

    @Override
    public void processSampleDescription(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        TimecodeSampleDescriptionAtom timecodeSampleDescriptionAtom = new TimecodeSampleDescriptionAtom(reader, atom);
        timecodeSampleDescriptionAtom.addMetadata(directory);
    }

    @Override
    public void processMediaInformation(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        TimecodeInformationMediaAtom timecodeInformationMediaAtom = new TimecodeInformationMediaAtom(reader, atom);
        timecodeInformationMediaAtom.addMetadata(directory);
    }

    @Override
    protected void processTimeToSample(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        // Do nothing
    }
}
