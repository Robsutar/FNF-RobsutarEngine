package com.robsutar.Engine.Audio;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Threads.PausableThread;

import javax.sound.sampled.Clip;
import javax.swing.plaf.synth.SynthTableHeaderUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Music {
    Clip audio;

    private float bpm = 190;
    long lastAge = 0;

    public int compass = 4;
    public int bpmDivisor = compass*4;

    private boolean running = false;

    private final Thread thread;

    public Music(Clip wav){
        audio = wav;
        thread = new Thread(()->{
            while (running){
                if (audio.isRunning()){
                    while (lastAge!=getMusicAge()) {
                        lastAge+=Math.signum(getMusicAge()-lastAge);
                        bpmTick();
                    }
                }
            }
        });
    }

    public void play(){
        audio.start();
    }

    public void pause(){
        audio.stop();
    }

    public void pauseOrStart(){
        if (audio.isActive()){pause();}
        else {play();}
    }

    public void setBpm(float b){
        bpm = b;

        lastAge = getAge();
    }

    public float getBpm(){
        return bpm;
    }

    private void bpmTick(){
        Handler.bpmTick(lastAge);
    }

    public long getAge(){
        return lastAge;
    }

    private long getMusicAge(){
        return (long) (bpm*(audio.getMicrosecondPosition()*Math.pow(10,-6d))/(60d/bpmDivisor));
    }

    public void initialize(){
        running = true;
        thread.start();
    }
    public void finish(){
        running = false;
    }
}
