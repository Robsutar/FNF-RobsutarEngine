package com.robsutar;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.FileManager;

import java.awt.image.BufferedImage;

public class Assets {
    public static BufferedImage ARROW_UP = FileManager.loadImage(FileManager.loadFile(FileManager.imagesPath+"arrowUp.png"));
    public static BufferedImage ARROW_DOWN = FileManager.loadImage(FileManager.loadFile(FileManager.imagesPath+"arrowDown.png"));
    public static BufferedImage ARROW_LEFT = FileManager.loadImage(FileManager.loadFile(FileManager.imagesPath+"arrowLeft.png"));
    public static BufferedImage ARROW_RIGHT = FileManager.loadImage(FileManager.loadFile(FileManager.imagesPath+"arrowRight.png"));

    public static Music DIE = new Music(FileManager.loadWav(FileManager.loadFile(FileManager.audioPath+"Die by Jeff Williams and Casey Lee Williams.wav")));
}
