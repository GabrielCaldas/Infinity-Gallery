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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.boxes;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.Mp4Dictionary;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.media.Mp4SoundDirectory;

import java.io.IOException;

/**
 * ISO/IED 14496-12:2015 pg.161
 */
public class AudioSampleEntry extends SampleEntry
{
    int channelcount;
    int samplesize;
    long samplerate;

    public AudioSampleEntry(SequentialReader reader, Box box) throws IOException
    {
        super(reader, box);

        reader.skip(8); // Reserved
        channelcount = reader.getUInt16();
        samplesize = reader.getInt16();
        reader.skip(2); // Pre-defined
        reader.skip(2); // Reserved
        samplerate = reader.getUInt32();
        // ChannelLayout()
        // DownMix and/or DRC boxes
        // More boxes as needed
    }

    public void addMetadata(Mp4SoundDirectory directory)
    {
        Mp4Dictionary.setLookup(Mp4SoundDirectory.TAG_AUDIO_FORMAT, format, directory);
        directory.setInt(Mp4SoundDirectory.TAG_NUMBER_OF_CHANNELS, channelcount);
        directory.setInt(Mp4SoundDirectory.TAG_AUDIO_SAMPLE_SIZE, samplesize);
    }
}
