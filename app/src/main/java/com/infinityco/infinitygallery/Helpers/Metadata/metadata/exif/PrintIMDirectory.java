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

package com.infinityco.infinitygallery.Helpers.Metadata.metadata.exif;

import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Directory;

import java.util.HashMap;

/**
 * These tags can be found in Epson proprietary metadata. The index values are 'fake' but
 * chosen specifically to make processing easier
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
@SuppressWarnings("WeakerAccess")
public class PrintIMDirectory extends Directory
{
    public static final int TagPrintImVersion = 0x0000;

    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<Integer, String>();

    static
    {
        _tagNameMap.put(TagPrintImVersion, "PrintIM Version");
    }

    public PrintIMDirectory()
    {
        this.setDescriptor(new PrintIMDescriptor(this));
    }

    @Override
    @NotNull
    public String getName()
    {
        return "PrintIM";
    }

    @Override
    @NotNull
    protected HashMap<Integer, String> getTagNameMap()
    {
        return _tagNameMap;
    }
}
