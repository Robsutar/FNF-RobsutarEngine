package com.robsutar.Engine.Files;

import com.robsutar.Engine.Helpers.FileManager;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageFile extends AssetsFile{
    public final BufferedImage image;
    public ImageFile(File file) {
        super(file);
        image= FileManager.loadImage(file);
    }
}
