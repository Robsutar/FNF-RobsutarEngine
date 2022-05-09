package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.FileManager;
import com.robsutar.Engine.Threads.BpmTicable;
import com.robsutar.Engine.Threads.KeyboardInteractive;
import com.robsutar.Engine.Threads.Ticable;
import com.robsutar.Engine.Window.RenderablePanel;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCAnimatedTab;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCAtlasManagerTab;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCDesignTab;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCTapBpm;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PCMainPanel extends RenderablePanel implements Ticable , KeyboardInteractive, BpmTicable {

    private final JPanel topPanel,bottomPanel;

    private final List<PCTab> tabs;
    public final PCDesignTab designTab;
    public final PCAtlasManagerTab atlasManagerTab;
    public final PCAnimatedTab animatedTab;
    public final PCTapBpm tapTab;
    PhaseConfigurationDialog phaseConfiguration;
    public PCTab lastTab;
    public final JSONObject phaseJson;

    private final Music music;
    public final String mapName;
    public final String musicName;
    public final String folder;
    public final String assetsFolder;
    public final PCAssets assets;
    JSlider musicSlider = new JSlider(0,1000000000);
    public boolean draggingSlider = false;

    public final Color color0 = EngineVisuals.BACKGROUND_COLOR_2;
    public final Color color1 = color0.darker();
    public final Color color2 = EngineVisuals.changeOpacity(EngineVisuals.BACKGROUND_COLOR_0,0.5f);
    public final Color color3 = EngineVisuals.SPRAY;

    public PCMainPanel(JSONObject phase){
        phaseJson=phase;
        mapName = String.valueOf(phase.get("name"));
        musicName = String.valueOf(phase.get("music"));
        folder = FileManager.phasesPath+mapName+"\\";
        assetsFolder = folder+"assets\\";
        this.music=new Music(FileManager.loadWav(FileManager.loadFile(assetsFolder+musicName)));

        setLayout(new BorderLayout());
        setBackground(color1);

        topPanel = new JPanel();
        topPanel.setBackground(color0);
        topPanel.setPreferredSize(new Dimension(50,50));
        topPanel.setLayout(new GridLayout());

        bottomPanel = new BottomPanel();

        tabs = new ArrayList<>();
        designTab = new PCDesignTab(this);
        atlasManagerTab = new PCAtlasManagerTab(this);
        tapTab = new PCTapBpm(this);
        assets = new PCAssets(this);
        animatedTab = new PCAnimatedTab(this);

        tabs.add(designTab);
        tabs.add(atlasManagerTab);
        tabs.add(tapTab);
        tabs.add(animatedTab);

        lastTab = designTab;
        add(topPanel,BorderLayout.NORTH);
        add(bottomPanel,BorderLayout.SOUTH);
        add(lastTab.getPanel(),BorderLayout.CENTER);

        phaseConfiguration = new PhaseConfigurationDialog(this);

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
        JButton animated = new JButton("Animated");
        animated.addActionListener(e ->{openTab(animatedTab);});
        topButtons.add(animated);

        for(JButton b:topButtons){
            b.setFocusable(false);
            b.setForeground(Color.WHITE);
            b.setBackground(topPanel.getBackground());
            b.setFont(getFont().deriveFont(32f));
            b.setOpaque(false);
            b.setBorderPainted(false);
            topPanel.add(b);
        }

        save();
        spawnAll();
    }

    @Override
    public void onOpen() {

    }

    public void update(){
        revalidate();
        repaint();
    }
    public void openPhaseConfiguration(){
        getMusic().pause();

        phaseConfiguration.open();
    }

    public void changeMusicPos(long position){
        music.setPosition(position);
    }

    public long getMusicPosition(){return music.getPosition();}

    private void openTab(PCTab tab){
        remove(lastTab.getPanel());
        lastTab = tab;
        add(lastTab.getPanel(),BorderLayout.CENTER);
        update();
    }
    int tickAge = 0;
    @Override
    public void tick() {
        tickAge++;
        if (draggingSlider&&tickAge%5==0){
            changeMusicPos((long) (music.getMusicMaxAge()*((double)musicSlider.getValue()/musicSlider.getMaximum())));
        }else if (music.isActive()&&!draggingSlider){
            musicSlider.setValue((int)(musicSlider.getMaximum()*((double)music.getPosition()/music.getMusicMaxAge())));
        }
        for (PCTab t:tabs){
            t.tick();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            music.pauseOrStart();
        } else if (e.getKeyCode()==KeyEvent.VK_F6){
            openPhaseConfiguration();
        }else if (e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_S){
            save();
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

    public void save(){
        JSONObject exit = new JSONObject();
        exit.put("music",musicName);
        exit.put("mapping",designTab.getPhaseJson());
        exit.put("name",mapName);
        exit.put("assets",assets.getAssetsJson());
        FileManager.writeFile(exit,FileManager.loadFile(folder+"phase.json"));
    }

    public Music getMusic() {return music;}

    private class BottomPanel extends JPanel{
        private BottomPanel(){
            setLayout(new BorderLayout());
            setBackground(color0);
            setPreferredSize(new Dimension(50,50));
            musicSlider.setFocusable(false);
            musicSlider.setValue(0);
            musicSlider.addMouseListener(new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    draggingSlider=true;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    changeMusicPos((long) (music.getMusicMaxAge()*((double)musicSlider.getValue()/musicSlider.getMaximum())));
                    draggingSlider=false;
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseDragged(MouseEvent e) {

                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
            add(musicSlider,BorderLayout.CENTER);
        }
    }
}
