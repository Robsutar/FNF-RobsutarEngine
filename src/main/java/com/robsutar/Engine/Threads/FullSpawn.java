package com.robsutar.Engine.Threads;

public interface FullSpawn {
    default void spawnAll(){
        if (this instanceof  Ticable){
            ((Ticable) this).SpawnTick();
        }
        if (this instanceof  KeyboardInteractive){
            ((KeyboardInteractive) this).SpawnKeyboardInteractive();
        }
        if (this instanceof  Renderable){
            ((Renderable) this).SpawnRender();
        }
        if (this instanceof  BpmTicable){
            ((BpmTicable) this).SpawnBpm();
        }
    }
    default void killAll(){
        if (this instanceof  Ticable){
            ((Ticable) this).KillTick();
        }
        if (this instanceof  KeyboardInteractive){
            ((KeyboardInteractive) this).KillKeyboardInteractive();
        }
        if (this instanceof  Renderable){
            ((Renderable) this).KillRender();
        }
        if (this instanceof  BpmTicable){
            ((BpmTicable) this).KillBpm();
        }
    }
}
