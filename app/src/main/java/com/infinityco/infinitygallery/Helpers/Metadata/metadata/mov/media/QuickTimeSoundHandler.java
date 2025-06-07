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
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.*;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.Atom;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.SoundInformationMediaHeaderAtom;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mov.atoms.SoundSampleDescriptionAtom;

import java.io.IOException;

/**
 * @author Payton Garland
 */
public class QuickTimeSoundHandler extends QuickTimeMediaHandler<QuickTimeSoundDirectory>
{
    public QuickTimeSoundHandler(Metadata metadata)
    {
        super(metadata);
    }

    @NotNull
    @Override
    protected QuickTimeSoundDirectory getDirectory()
    {
        return new QuickTimeSoundDirectory();
    }

    @Override
    protected String getMediaInformation()
    {
        return QuickTimeAtomTypes.ATOM_SOUND_MEDIA_INFO;
    }

    @Override
    public void processSampleDescription(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        SoundSampleDescriptionAtom soundSampleDescriptionAtom = new SoundSampleDescriptionAtom(reader, atom);
        soundSampleDescriptionAtom.addMetadata(directory);
    }

    @Override
    public void processMediaInformation(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        SoundInformationMediaHeaderAtom soundInformationMediaHeaderAtom = new SoundInformationMediaHeaderAtom(reader, atom);
        soundInformationMediaHeaderAtom.addMetadata(directory);
    }

    @Override
    protected void processTimeToSample(@NotNull SequentialReader reader, @NotNull Atom atom) throws IOException
    {
        directory.setDouble(QuickTimeSoundDirectory.TAG_AUDIO_SAMPLE_RATE, QuickTimeHandlerFactory.HANDLER_PARAM_TIME_SCALE);
    }
}
