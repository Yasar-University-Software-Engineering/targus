package com.targus.algorithm.ga;

import com.targus.utils.LongRunningTask;
import com.targus.utils.TimeOutTask;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

public class TimeBasedTerminal implements TerminalState {

    private Instant startTime;
    private TimeOutTask timeOutTask;
    private long currentTimeInSeconds;
    private final long endTimeInSeconds;

    public TimeBasedTerminal(int minutes) {
        currentTimeInSeconds = 0;
        endTimeInSeconds = minutes * 60L;
        start();
    }

    private void start() {
        Thread thread = new Thread(new LongRunningTask());
        thread.start();
        startTime = Instant.now();

        Timer timer = new Timer();
        timeOutTask = new TimeOutTask(thread, timer);
        timer.schedule(timeOutTask, endTimeInSeconds * 1000L);
    }

    @Override
    public boolean isTerminal() {
        return !timeOutTask.isAlive();
    }

    @Override
    public void nextState() {
        Instant endTime = Instant.now();
        currentTimeInSeconds = Duration.between(startTime, endTime).toSeconds();
    }

    @Override
    public long getCurrentState() {
        return currentTimeInSeconds;
    }

    @Override
    public long getFinishState() {
        return endTimeInSeconds;
    }
}
