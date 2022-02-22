package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Assets;
import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Threads.BpmTicable;
import com.robsutar.Engine.Threads.KeyboardInteractive;
import com.robsutar.Engine.Threads.Ticable;
import com.robsutar.Engine.Window.RenderablePanel;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCAtlasManagerTab;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCDesignTab;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCTapBpm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PCMainPanel extends RenderablePanel implements Ticable , KeyboardInteractive, BpmTicable {

    private JPanel topPanel,bottomPanel;

    private List<PCTab> tabs;
    private PCDesignTab designTab;
    private PCAtlasManagerTab atlasManagerTab;
    private PCTapBpm tapTab;
    private PCTab lastTab;

    public Music music = Assets.DIE;

    public final Color color0 = EngineVisuals.BACKGROUND_COLOR_2;
    public final Color color1 = color0.darker();
    public final Color color2 = EngineVisuals.changeOpacity(EngineVisuals.BACKGROUND_COLOR_0,0.5f);
    public final Color color3 = EngineVisuals.SPRAY;

    public PCMainPanel(){

        setLayout(new BorderLayout());
        setBackground(color1);

        topPanel = new JPanel();
        topPanel.setBackground(color0);
        topPanel.setPreferredSize(new Dimension(50,50));
        topPanel.setLayout(new GridLayout());

        bottomPanel = new JPanel();
        bottomPanel.setBackground(color0);
        bottomPanel.setPreferredSize(new Dimension(50,50));

        List<JButton> topButtons = new ArrayList<>();

        JButton designButton = new JButton("Design");
        designButton.addActionListener(e ->{openTab(designTab);});
        topButtons.add(designButton);
        JButton tap = new JButton("Tap Bpm");
        tap.addActionListener(e ->{openTab(tapTab);});
        topButtons.add(tap);
        JButton atlasManager = new JButton("Atlas Manager");
        atlasManager.addActionListener(e ->{openTab(atlasManagerTab);});
        topButtons.add(atlasManager);

        for(JButton b:topButtons){
            b.setFocusable(false);
            b.setForeground(Color.WHITE);
            b.setBackground(topPanel.getBackground());
            b.setFont(getFont().deriveFont(32f));
            b.setOpaque(false);
            b.setBorderPainted(false);
            topPanel.add(b);
        }

        tabs = new ArrayList<>();
        designTab = new PCDesignTab(this);
        atlasManagerTab = new PCAtlasManagerTab(this);
        tapTab = new PCTapBpm(this);

        tabs.add(designTab);
        tabs.add(atlasManagerTab);
        tabs.add(tapTab);

        lastTab = tapTab;
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel,BorderLayout.SOUTH);
        add(lastTab.getPanel(),BorderLayout.CENTER);

        spawnAll();

        music.initialize();
    }

    public void update(){
        revalidate();
        repaint();
    }

    private void openTab(PCTab tab){
        remove(lastTab.getPanel());
        lastTab = tab;
        add(lastTab.getPanel(),BorderLayout.CENTER);
        update();
    }
    @Override
    public void tick() {
        for (PCTab t:tabs){
            t.tick();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            music.pauseOrStart();
        }
        for (PCTab t:tabs){
            t.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (PCTab t:tabs){
            t.keyReleased(e);
        }
    }

    @Override
    public void bpmTick(long age) {
        for (PCTab t:tabs){
            t.bpmTick(age);
        }
    }
}
