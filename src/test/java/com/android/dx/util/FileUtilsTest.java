package com.android.dx.util;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileUtilsTest {
    @Test
    @Order(1)
    public void getClassesRecursively() throws IOException {
        List<String> paths = FileUtils.getClassesRecursively(Paths.get("target"));
        System.out.println(paths.size() + " entries");
        System.out.println(Arrays.toString(paths.toArray()));
    }

    @Test
    @Order(2)
    public void getClassFilesRecursively() {
        // TODO Remove timing
        long startTime = System.nanoTime();
        List<String> paths = FileUtils.getClassFilesRecursively(Paths.get("target").toFile());
        long endTime = System.nanoTime();
        long elapseSeconds = (endTime - startTime) / (1000 * 1000);
        System.out.println("getClassFilesRecursively(\"" + Paths.get("target").toFile().getAbsolutePath() + "\") = " + elapseSeconds + " msec");

        System.out.println(paths.size() + " entries");
        System.out.println(Arrays.toString(paths.toArray()));
    }
}
