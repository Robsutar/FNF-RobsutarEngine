package com.robsutar.Engine.Window;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.PauseableThread;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class WindowManager {
    private static MainFrame frame = new MainFrame();;
    private static RenderablePanel actualPanel;
    private static JPanel lastPanel;

    public static void setActualPanel(RenderablePanel panel){
        Handler.dispose();
        if (lastPanel!=null){
            frame.remove(lastPanel);
        }
        actualPanel = panel;
        lastPanel = panel;
        frame.add(lastPanel);
        frame.invalidate();
        frame.validate();
        frame.repaint();
        panel.onOpen();
    }

    public static void keyDown(KeyEvent e){
        actualPanel.keyDown(e);
    }
    public static void keyUp(KeyEvent e){
        actualPanel.keyUp(e);
    }

    public static int getWidth(){
        return frame.getWidth();
    }
    public static int getHeight(){
        return frame.getHeight();
    }

    public static MainFrame getFrame() {
        return frame;
    }
    public static RenderablePanel getPanel(){
        return actualPanel;
    }
}
