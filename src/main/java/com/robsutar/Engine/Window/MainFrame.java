package com.robsutar.Engine.Window;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.EngineConfigs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrame extends JFrame implements KeyListener {

    private static Thread tickThread;
    private static Thread renderThread;

    public MainFrame(){
        //configs
        Dimension dim = new Dimension(1280,720);

        setTitle("Robsutar com.robsutar.Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setFocusable(true);

        setPreferredSize(dim);

        setVisible(true);
        pack();
        setLocationRelativeTo(null);


        //threads
        addKeyListener(this);

        tickThread = new Thread(() ->{
            long now;
            long total;
            while (true) {
                long fpsTime = (long) ((1000.0 /60) * 1000000.0);
                now = System.nanoTime();
                Handler.tick();
                try {
                    total = System.nanoTime() - now;
                    if(total > fpsTime) {
                        continue;
                    }
                    Thread.sleep((fpsTime - (System.nanoTime() - now)) / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tickThread.start();

        renderThread = new Thread(() ->{
            long now;
            long total;
            while (true) {
                long fpsTime = (long) ((1000.0 / EngineConfigs.fps) * 1000000.0);
                now = System.nanoTime();
                if (WindowManager.getPanel()!=null) {
                    WindowManager.getPanel().render();
                }
                try {
                    total = System.nanoTime() - now;
                    if(total > fpsTime) {
                        continue;
                    }
                    Thread.sleep((fpsTime - (System.nanoTime() - now)) / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        renderThread.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Handler.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Handler.keyReleased(e);
    }
}
