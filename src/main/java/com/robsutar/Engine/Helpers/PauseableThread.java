package com.robsutar.Engine.Helpers;

import com.robsutar.Engine.Handler;
import com.robsutar.Engine.Threads.SimpleThread;
import com.robsutar.Engine.Window.WindowManager;

public abstract class PauseableThread extends Thread implements SimpleThread {
    private boolean paused = false;
    private boolean running = true;
    final Object pauseLock = new Object();

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) { // may have changed while waiting to
                    // synchronize on pauseLock
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait(); // will cause this Thread to block until
                            // another thread calls pauseLock.notifyAll()
                            // Note that calling wait() will
                            // relinquish the synchronized lock that this
                            // thread holds on pauseLock so another thread
                            // can acquire the lock to call notifyAll()
                            // (link with explanation below this code)
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) { // running might have changed since we paused
                        break;
                    }
                }
            }
            code();
            try {
                sleep(waitTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void code();
    public abstract long waitTime();

    public void pause() {
        paused = true;
    }
    public void unPause() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

    @Override
    public synchronized void start() {
        spawnAll();
    }

    @Override
    public void spawnAll() {
        super.start();
        SimpleThread.super.spawnAll();
    }

    @Override
    public void killAll() {
        running=false;
        SimpleThread.super.killAll();
    }
}
