package com.targus.algorithm.ga;

import com.targus.utils.LongRunningTask;
import com.targus.utils.TimeOutTask;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

public class TimeBasedTerminal implements TerminalState {

    private Instant startTime;
    private TimeOutTask timeOutTask;
    private long currentTimeInMilliseconds;
    private final long endTimeInMilliseconds;

    public TimeBasedTerminal(int seconds) {
        currentTimeInMilliseconds = 0;
        endTimeInMilliseconds = seconds * 1000L;
        start();
    }

    private void start() {
        Thread thread = new Thread(new LongRunningTask());
        thread.start();
        startTime = Instant.now();

        Timer timer = new Timer();
        timeOutTask = new TimeOutTask(thread, timer);
        timer.schedule(timeOutTask, endTimeInMilliseconds);
    }

    @Override
    public boolean isTerminal() {
        return !timeOutTask.isAlive();
    }

    @Override
    public void nextState() {
        Instant endTime = Instant.now();
        currentTimeInMilliseconds = Duration.between(startTime, endTime).toMillis();
    }

    @Override
    public long getCurrentState() {
        return currentTimeInMilliseconds;
    }

    @Override
    public long getFinishState() {
        return endTimeInMilliseconds;
    }
}
