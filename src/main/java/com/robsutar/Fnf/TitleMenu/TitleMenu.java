package com.robsutar.Fnf.TitleMenu;

import com.robsutar.Assets;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Window.RenderablePanel;
import com.robsutar.Engine.Window.WindowManager;
import com.robsutar.Fnf.PhaseCreator.PCMainPanel;
import com.robsutar.Fnf.PhaseCreator.PCMusicChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class TitleMenu extends RenderablePanel {
    public TitleMenu(){
        JButton play = new JButton("Play");
        JButton create = new JButton("Create");
        JButton open = new JButton("Open");
        
        play.addActionListener(e -> {

        });

        open.addActionListener(e -> {
            File f = FileManager.loadExplorerFile(new FileNameExtensionFilter("JSON files","json"));
            if (f!=null){
                WindowManager.setActualPanel(new PCMainPanel(FileManager.loadJson(f)));
            }
            });

        create.addActionListener(e -> {
            PCMusicChooser pcmc = new PCMusicChooser();
            pcmc.setVisible(true);
        });

        add(play);
        add(create);
        add(open);
    }
}
