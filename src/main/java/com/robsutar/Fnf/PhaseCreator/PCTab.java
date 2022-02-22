package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Engine.Helpers.EngineVisuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class PCTab{

    protected final JPanel panel;
    protected final PCMainPanel handler;

    public PCTab(PCMainPanel h){
        handler=h;
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
    }

    public JPanel getPanel(){
        return panel;
    }

    protected void tick(){

    }
    protected void keyPressed(KeyEvent e){

    }

    protected void keyReleased(KeyEvent e){

    }

    public void bpmTick(long age) {
    }
}
