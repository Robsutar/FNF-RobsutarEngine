package com.robsutar.Engine.Helpers;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class KeyManager{

    private static List<Integer> codes = new ArrayList<>();

    public static boolean isKeyPressed(int keyCode){
        return codes.contains(keyCode);
    }

    public static void keyPressed(KeyEvent e) {
        if (!codes.contains(e.getKeyCode()))
            codes.add(e.getKeyCode());
    }

    public static void keyReleased(KeyEvent e) {
        codes.remove((Integer) e.getKeyCode());
    }

    public static void clearKeyPressHistory(){codes.clear();}
}
