package com.robsutar;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Fnf.AnimationBuilder.Animation;
import com.robsutar.Fnf.AnimationBuilder.AnimationFileHook;
import com.robsutar.Fnf.AnimationLoader.AnimFileHook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

    public static ImageIcon BAIXAR = getIconBy25px("baixar.png");
    public static ImageIcon CURSOR = getIconBy25px("cursor.png");
    public static ImageIcon LOCATION = getIconBy25px("location.png");
    public static ImageIcon MAIS = getIconBy25px("mais.png");
    public static ImageIcon MENOS = getIconBy25px("menos.png");

    public static AnimationFileHook ARROW_ANIMATION = new AnimationFileHook(FileManager.loadFile(FileManager.mainAssetsPath+"animated\\NOTE_assets.json"));
    public static AnimFileHook ARROW_ANIM = new AnimFileHook(FileManager.loadFile(FileManager.mainAssetsPath+"animated\\NOTE_assets.json"));

    //public static Music DIE = new Music(FileManager.loadWav(FileManager.loadFile(FileManager.audioPath+"Die by Jeff Williams and Casey Lee Williams.wav")));
    //public static Music DRUM_HIT = new Music(FileManager.loadWav(FileManager.loadFile(FileManager.audioPath+"drum-hitnormal.wav")));
    //public static Music PICK = new Music(FileManager.loadWav(FileManager.loadFile(FileManager.audioPath+"Pick It Up.wav")));

    private static ImageIcon getIconBy25px(String fileName){
        BufferedImage image = FileManager.loadImage(FileManager.loadFile(FileManager.imagesPath+"icons\\"+fileName));
        Image newImage = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
