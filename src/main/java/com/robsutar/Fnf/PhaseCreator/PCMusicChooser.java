package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Window.WindowManager;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class PCMusicChooser extends JDialog {
    File musicFile;
    String mapName="";
    public PCMusicChooser(){
        setResizable(false);
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setTitle("Phase Creator");
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3,1));

        JLabel nameLabel = new JLabel("Map Name: ");
        JTextField nameField = new JTextField();

        JLabel musicLabel = new JLabel("Map Music: ");
        JButton musicButton = new JButton("load");

        musicButton.addActionListener(e -> {
            File f = FileManager.loadExplorerFile(new FileNameExtensionFilter("WAV files","wav"));
            if (f!=null){
                musicButton.setText(f.getName());
                musicFile = f;
                musicLabel.setForeground(Color.BLACK);
            }
        });

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            boolean missing = false;
            nameLabel.setForeground(Color.black);
            musicLabel.setForeground(Color.black);
            mapName = nameField.getText();
            if (musicFile==null){
                musicLabel.setForeground(Color.red);
                missing = true;
            }
            if (mapName.length()<1){
                nameLabel.setForeground(Color.red);
                missing=true;
            }
            if (!missing){
                dispose();

                JSONObject phase = new JSONObject();

                phase.put("music",musicFile.getName());

                phase.put("name",mapName);

                FileManager.writeFile(phase,FileManager.loadFile(FileManager.phasesPath+mapName+"\\phase.json"));
                FileManager.writeFile(musicFile,FileManager.loadFile(FileManager.phasesPath+mapName+"assets\\"+musicFile.getName()));
                PCMainPanel creator = new PCMainPanel(phase);
                WindowManager.setActualPanel(creator);
            }
        });

        add(makeLinePanel(nameLabel,nameField));
        add(makeLinePanel(musicLabel,musicButton));
        add(okButton);
    }
    private Panel makeLinePanel(JLabel label,Component field){
        Panel p = new Panel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        p.add(label);
        c.gridx=1;
        p.add(field);
        return p;
    }
}
