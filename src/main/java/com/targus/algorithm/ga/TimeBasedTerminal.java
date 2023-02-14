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
        timer.schedule(timeOutTask, minutes * 60 * 1000L);
    }

    @Override
    public boolean isTerminal() {
        return !timeOutTask.isAlive();
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
