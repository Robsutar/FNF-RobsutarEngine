package com.robsutar;

import com.robsutar.Fnf.RenderableObjects.Visual.FallingSnow;
import com.robsutar.Engine.Window.WindowManager;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args){
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e){
            e.printStackTrace();
        }
        WindowManager.setActualPanel(new PCMainPanel());

        new FallingSnow(10,5);

        String goodbye = "C:\\Users\\Robson\\Downloads\\Krewella - Say Goodbye (Speed Up Ver).mp3";

        String introd = "E:\\RobsutarProjects\\RobsutarFNF\\resources\\assets\\sounds\\introduction.wav";
    }
}
