package com.robsutar;

import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Helpers.PrintColor;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Fnf.RenderableObjects.Visual.FallingSnow;
import com.robsutar.Engine.Window.WindowManager;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.formdev.flatlaf.FlatLightLaf;
import com.robsutar.Fnf.TitleMenu.TitleMenu;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e){e.printStackTrace();}

        //FileManager.writeFile(FileManager.loadFile("C:\\Users\\Robson\\Downloads\\Die by Jeff Williams and Casey Lee Williams.wav"),FileManager.loadFile("E:\\Tudo\\rwby.wav"));

        WindowManager.setActualPanel(new TitleMenu());
        //WindowManager.setActualPanel(new PCMainPanel(Assets.DIE,"E:\\RobsutarProjects\\FNF-RobsutarEngine\\src\\main\\resources\\assets\\phases\\vsRwby"));

        new FallingSnow(10,5);
    }
}
