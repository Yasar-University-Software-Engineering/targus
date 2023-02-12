package com.targus.algorithm.ga;

import com.targus.utils.LongRunningTask;
import com.targus.utils.TimeOutTask;

import java.util.Timer;

public class TimeBasedTerminal implements TerminalState {

    private int minutes;
    private TimeOutTask timeOutTask;

    public TimeBasedTerminal(int minutes) {
        this.minutes = minutes;
        start();
    }

    private void start() {
        Thread thread = new Thread(new LongRunningTask());
        thread.start();

        Timer timer = new Timer();
        timeOutTask = new TimeOutTask(thread, timer);
        timer.schedule(timeOutTask, 60 * 1000);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public void nextState() {

    }

    @Override
    public int getCurrentState() {
        return 0;
    }

    @Override
    public int getFinishState() {
        return 0;
    }
}
