package com.robsutar.Engine.Window;


import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.GraphicsManipulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.Key;

public abstract class RenderablePanel extends JPanel {

    public RenderablePanel(){
        setBackground(Color.black);
    }

    public void render(){
        repaint();
    }

    public abstract void onOpen();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        GraphicsManipulator.standardTransform = g2d.getTransform();
        Handler.render(g2d);
    }

    protected void keyDown(KeyEvent e){

    }
    protected void keyUp(KeyEvent e){

    }
}
