package com.robsutar.Engine.Audio;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Helpers.SystemPrinter;
import com.robsutar.Engine.Threads.PausableThread;

import javax.sound.sampled.Clip;
import javax.swing.plaf.synth.SynthTableHeaderUI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Music {
    Clip audio;

    private double bpm = 60;

    public int compass = 4;

    public Music(Clip wav){
        audio = wav;
        /*
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

         */
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

    public void setBpm(double b){
        bpm = b;
    }

    public double getBpm(){
        return bpm;
    }

    /*
    private long getMusicAge(){
        return (long) (bpm*(audio.getMicrosecondPosition()*Math.pow(10,-6d))/(60d/bpmDivisor));
    }

     */

    public boolean isActive(){return audio.isActive();}

    public long getMusicMaxAge(){
        return audio.getMicrosecondLength();
    }

    public long getPosition() {
        return audio.getMicrosecondPosition();
    }

    public void setPosition(long i) {
        audio.setMicrosecondPosition(i);
    }
}
