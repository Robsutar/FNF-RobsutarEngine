package com.robsutar.Engine.Threads;

import com.robsutar.Engine.Handler;

public interface BpmTicable extends FullSpawn{
    default void SpawnBpm(){
        Handler.addObject(this);
    }
    default void KillBpm(){
        Handler.removeObject(this);
    }
    void bpmTick(long age);
}
