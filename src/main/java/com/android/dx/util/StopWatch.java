package com.android.dx.util;

public class StopWatch {
    private final long startTimeMillis;

    public StopWatch() {
        startTimeMillis = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final long millis = System.currentTimeMillis() - startTimeMillis;
        final long seconds = millis / 1000;
        final long HH = seconds / 3600;
        final long MM = (seconds % 3600) / 60;
        final long SS = seconds % 60;
        final long SSS = millis % 1000;
        return String.format("%02d:%02d:%02d.%03d", HH, MM, SS, SSS);
    }
}
