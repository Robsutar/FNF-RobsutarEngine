package com.robsutar.Engine.Window;

import javax.swing.*;

public final class WindowManager {
    private static MainFrame frame = new MainFrame();;
    private static RenderablePanel actualPanel;
    private static JPanel lastPanel;

    public static void setActualPanel(RenderablePanel panel){
        if (lastPanel!=null){
            frame.remove(lastPanel);
        }
        actualPanel = panel;
        lastPanel = panel;
        frame.add(lastPanel);
        frame.invalidate();
        frame.validate();
        frame.repaint();
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
