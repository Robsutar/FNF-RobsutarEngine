package com.robsutar.Engine.Threads;

import com.robsutar.Engine.Handler;

public interface Ticable extends FullSpawn {
    default void SpawnTick(){
        Handler.addObject(this);
    }
    default void KillTick(){
        Handler.removeObject(this);
    }
    void tick();
}
