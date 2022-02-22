package com.robsutar.Engine.Threads;

import com.robsutar.Engine.Handler;

import java.awt.event.KeyEvent;

public interface KeyboardInteractive extends FullSpawn{
    default void SpawnKeyboardInteractive(){
        Handler.addObject(this);
    }
    default void KillKeyboardInteractive(){
        Handler.removeObject(this);
    }
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}
