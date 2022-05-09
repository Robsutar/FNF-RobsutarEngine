package com.robsutar.Fnf.PhaseInit.PhaseBuilder;

import com.robsutar.Fnf.PhaseInit.PhaseContents;

import javax.swing.*;
import java.awt.event.KeyEvent;

public abstract class PBWindow  extends JPanel implements PBColors {
    final PBMainPanel handler;
    public PBWindow(PBMainPanel handler){
        this.handler=handler;
        setOpaque(false);
    }

    protected PhaseContents getContents(){
        return handler.contents;
    }

    public void bpmTick(){

    }

    public void keyPressed(KeyEvent e){

    }
    public void keyReleased(KeyEvent e){

    }
}
