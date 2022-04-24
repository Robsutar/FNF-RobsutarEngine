package com.robsutar.Fnf;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class KeyConfiguration {
    public static final KeyList ARROW_UP = new KeyList(KeyEvent.VK_W, KeyEvent.VK_UP);
    public static final KeyList ARROW_DOWN = new KeyList(KeyEvent.VK_S, KeyEvent.VK_DOWN);
    public static final KeyList ARROW_LEFT = new KeyList(KeyEvent.VK_A, KeyEvent.VK_LEFT);
    public static final KeyList ARROW_RIGHT = new KeyList(KeyEvent.VK_D, KeyEvent.VK_RIGHT);

    public static boolean keyEquals(KeyEvent e,KeyList key){
        for(int k:key.keys){
            if (k==e.getKeyCode())
                return true;
        }return false;
    }

    public static boolean keyEquals(int keyCode,KeyList key){
        for(int k:key.keys){
            if (k==keyCode)
                return true;
        }return false;
    }
    private static class KeyList{
        public final List<Integer> keys;

        public KeyList(List<Integer> keys) {
            this.keys = keys;
        }
        public KeyList(int key1) {
            this.keys = new ArrayList<>();
            keys.add(key1);
        }
        public KeyList(int key1,int key2) {
            this.keys = new ArrayList<>();
            keys.add(key1);
            keys.add(key2);
        }
    }
}
