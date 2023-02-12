package com.targus.utils;

public class LongRunningTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < Long.MAX_VALUE; i++) {
            if(Thread.interrupted()) {
                return;
            }
        }
    }
}
