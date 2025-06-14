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
package com.infinityco.infinitygallery.Helpers.Metadata.metadata.heif;

import com.infinityco.infinitygallery.Helpers.Metadata.imaging.heif.HeifHandler;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialByteArrayReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.SequentialReader;
import com.infinityco.infinitygallery.Helpers.Metadata.lang.annotations.NotNull;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.heif.boxes.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Payton Garland
 */
public class HeifBoxHandler extends HeifHandler<HeifDirectory>
{
    HandlerBox handlerBox;

    private HeifHandlerFactory handlerFactory = new HeifHandlerFactory(this);

    public HeifBoxHandler(Metadata metadata)
    {
        super(metadata);
    }

    @Override
    protected HeifDirectory getDirectory()
    {
        return new HeifDirectory();
    }

    @Override
    public boolean shouldAcceptBox(@NotNull Box box)
    {
        List<String> boxes = Arrays.asList(HeifBoxTypes.BOX_FILE_TYPE,
            HeifBoxTypes.BOX_HANDLER,
            HeifBoxTypes.BOX_HVC1);

        return boxes.contains(box.type);
    }

    @Override
    public boolean shouldAcceptContainer(Box box)
    {
        return box.type.equals(HeifContainerTypes.BOX_METADATA)
            || box.type.equals(HeifContainerTypes.BOX_IMAGE_PROPERTY)
            || box.type.equals(HeifContainerTypes.BOX_ITEM_PROPERTY);
    }

    @Override
    public HeifHandler processBox(@NotNull Box box, @NotNull byte[] payload) throws IOException
    {
        if (payload != null) {
            SequentialReader reader = new SequentialByteArrayReader(payload);
            if (box.type.equals(HeifBoxTypes.BOX_FILE_TYPE)) {
                processFileType(reader, box);
            }else if (box.type.equals(HeifBoxTypes.BOX_HANDLER)) {
                handlerBox = new HandlerBox(reader, box);
                return handlerFactory.getHandler(handlerBox, metadata);
            }
        }
        return this;
    }

    @Override
    public void processContainer(@NotNull Box box, @NotNull SequentialReader reader) throws IOException
    {
        if (box.type.equals(HeifContainerTypes.BOX_METADATA)) {
            new FullBox(reader, box);
        }
    }

    private void processFileType(@NotNull SequentialReader reader, @NotNull Box box) throws IOException
    {
        FileTypeBox fileTypeBox = new FileTypeBox(reader, box);
        fileTypeBox.addMetadata(directory);
        if (!fileTypeBox.getCompatibleBrands().contains("mif1")) {
            directory.addError("File Type Box does not contain required brand, mif1");
        }
    }
}
