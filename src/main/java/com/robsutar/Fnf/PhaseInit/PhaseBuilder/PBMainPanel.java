package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.PauseableThread;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Engine.Window.RenderablePanel;
import com.robsutar.Fnf.PhaseCreator.Tabs.PCDesignTab;
import com.robsutar.Fnf.PhaseInit.PhaseContents;
import com.robsutar.Fnf.PhaseInit.PhasePart;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

public class PBMainPanel extends RenderablePanel implements PBColors {

    public final PhaseContents contents;

    PBWindow actualWindow;
    PBDesign design;

    PauseableThread pauseableThread;
    long lastPit;

    public PBMainPanel(PhaseContents contents){
        this.contents = contents;
    }

    @Override
    public void onOpen() {
        setLayout(new BorderLayout());
        setBackground(backgroundMoreDarker);

        design = new PBDesign(this);
        actualWindow=design;

        add(actualWindow,BorderLayout.CENTER);

        lastPit = contents.pits.get(0);
        pauseableThread = new PauseableThread() {
            @Override
            public void code() {
                if (getMusic().isActive()){
                    long musicPit = lastPit;
                    int indexOf = contents.pits.indexOf(musicPit);
                    if (musicPit<getMusicPosition()) {
                        while (musicPit < getMusicPosition()) {
                            indexOf++;
                            musicPit = contents.pits.get(indexOf);
                        }
                    }else if(musicPit>getMusicPosition()) {
                        while (musicPit > getMusicPosition()) {
                            indexOf--;
                            musicPit = contents.pits.get(indexOf);
                        }
                    }
                    while (lastPit!=musicPit) {
                        int indexPit = contents.pits.indexOf(lastPit);
                        if (lastPit < musicPit) {
                            indexPit++;
                            lastPit = contents.pits.get(indexPit);
                        } else if (lastPit > musicPit) {
                            indexPit--;
                            lastPit = contents.pits.get(indexPit);
                        }
                        bpmTick(indexPit);
                    }
                }
            }

            @Override
            public long waitTime() {
                double bpm = getActualPart().bpm;
                return (long) (60/bpm*1000/16);
            }
        };
        pauseableThread.spawnAll();
        pauseableThread.pause();
    }

    @Override
    protected void keyDown(KeyEvent e) {
        super.keyDown(e);
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            resumeOrPauseMusic();
        }else if (e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_S){
            contents.saveContents();
        }
        actualWindow.keyPressed(e);
    }

    protected void keyReleased(KeyEvent e){actualWindow.keyReleased(e);}

    public Music getMusic(){return contents.music;}

    private void bpmTick(long age){
        Handler.bpmTick(age);
        actualWindow.bpmTick();
    }

    public PhasePart getActualPart(){
        return getPart(getMusicPosition());
    }

    public PhasePart getPart(long index){
        for (int i = contents.parts.size(); i > 0;i--){
            PhasePart p = contents.parts.get(i-1);
            if (p.index<index){
                return p;
            }
        }
        return contents.parts.get(0);
    }

    public void changeMusicPos(Long l){
        getMusic().setPosition(l);
    }
    public Long getMusicPosition(){
        return getMusic().getPosition();
    }

    public void pauseMusic(){
        getMusic().pause();
        pauseableThread.pause();
    }
    public void resumeMusic(){
        getMusic().play();
        pauseableThread.unPause();
    }
    public void resumeOrPauseMusic(){
        if (getMusic().isActive()){
            pauseMusic();
        }else{
            resumeMusic();
        }
    }
}
