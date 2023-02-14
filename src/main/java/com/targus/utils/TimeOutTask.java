package com.targus.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimeOutTask extends TimerTask {

    private Thread thread;
    private Timer timer;
    private boolean isAlive;

    public TimeOutTask(Thread thread, Timer timer) {
        this.thread = thread;
        this.timer = timer;
        this.isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void run() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            timer.cancel();
            isAlive = false;
        }
    }
}
