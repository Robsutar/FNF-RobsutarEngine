package com.robsutar;

import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Fnf.PhaseInit.PhaseBuilder.PBMainPanel;
import com.robsutar.Fnf.PhaseInit.PhaseContents;
import com.robsutar.Fnf.RenderableObjects.Visual.FallingSnow;
import com.robsutar.Engine.Window.WindowManager;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args){
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e){e.printStackTrace();}

        //FileManager.writeFile(FileManager.loadFile("C:\\Users\\Robson\\Downloads\\Die by Jeff Williams and Casey Lee Williams.wav"),FileManager.loadFile("E:\\Tudo\\rwby.wav"));

        //WindowManager.setActualPanel(new TitleMenu());

        PhaseContents pc = new PhaseContents(FileManager.loadFile(FileManager.phasesPath+"vsMicca\\vsMicca-normal.json"));

        WindowManager.setActualPanel(new PBMainPanel(pc));

        //FileManager.writeFile(pc.getPhaseFile(),FileManager.loadFile(FileManager.phasesPath+"vsMicca\\phase.json"));

        new FallingSnow(10,5);
    }
}
