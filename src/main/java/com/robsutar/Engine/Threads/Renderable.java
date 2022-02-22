package com.robsutar.Engine.Threads;

import com.robsutar.Engine.Handler;

import java.awt.*;

public interface Renderable extends FullSpawn {
    default void SpawnRender(){
        Handler.addObject(this);
    }
    default void KillRender(){
        Handler.removeObject(this);
    }
    void render(Graphics2D g2d);
}
