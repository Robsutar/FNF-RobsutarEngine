package com.robsutar.Fnf.PhaseCreator;

import com.robsutar.Engine.Audio.Music;
import com.robsutar.Engine.Helpers.EngineVisuals;
import com.robsutar.Engine.Helpers.GraphicsManipulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public abstract class PCTab{

    protected final BackPanel panel;
    protected final PCMainPanel handler;

    public PCTab(PCMainPanel h){
        handler=h;
        panel = new BackPanel();
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

    protected void onPaintBackground(Graphics2D g2d){

    }

    public void bpmTick(long age) {
    }
    protected Music getMusic(){return handler.getMusic();}

    public class BackPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform at = g2d.getTransform();
            onPaintBackground(g2d);
            GraphicsManipulator.resetGraphics(g2d,at);
        }
    }
}
