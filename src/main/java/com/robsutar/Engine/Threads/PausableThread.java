package com.robsutar.Engine.Threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class PausableThread implements Runnable{

    private boolean suspend = true;

    private boolean running = false;

    public PausableThread(){
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        long last = 0;
        while (true) {
            long bpmTime = nanosecondsWait();
            long now = System.nanoTime();
            action();
            try {
                double finalDelta = (bpmTime-((System.nanoTime()-last)/1000000d));
                int waitNanos = (int) ((finalDelta%1)*1000000d);
                long waitMillis =(long) ((finalDelta-(finalDelta%1))/1000000d);

                //System.out.println(waitMillis+" <> "+waitNanos);

                if (waitNanos<0){waitNanos = 0;}
                if (waitMillis<0){waitMillis = 0;}

                last = System.nanoTime();
                Thread.sleep(waitMillis,waitNanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void action();
    public abstract long nanosecondsWait();
}
