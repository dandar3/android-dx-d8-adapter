package com.android.dx.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class StopWatchTest {
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        Assertions.assertEquals("00:00:00.000", stopWatch.toString());
    }
}
