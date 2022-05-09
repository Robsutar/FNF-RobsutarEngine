package com.robsutar.Engine.Threads;

import com.robsutar.Engine.Handler;

public interface SimpleThread extends FullSpawn{
    default void SpawnThread(){Handler.addObject(this);}
    default void KillThread(){
        Handler.removeObject(this);
    }
}
