package com.infinityco.infinitygallery.Helpers.Metadata.metadata.wav;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.Nullable;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.TagDescriptor;

/**
 * @author Payton Garland
 */
public class WavDescriptor extends TagDescriptor<WavDirectory>
{
    public WavDescriptor(@NotNull WavDirectory directory)
    {
        super(directory);
    }

    @Override
    @Nullable
    public String getDescription(int tagType)
    {
        return super.getDescription(tagType);
    }
}
