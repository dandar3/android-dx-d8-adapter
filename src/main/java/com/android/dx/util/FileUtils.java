package com.android.dx.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> getClassesRecursively(Path path) throws IOException {
        // TODO Remove timing
        long startTime = System.nanoTime();

        final List<String> classes = new ArrayList<>(1024);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            {
                final String absolutePath = file.toAbsolutePath().toString();
                if (absolutePath.endsWith(".class")){
                    classes.add(absolutePath);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // TODO Remove timing
        long endTime = System.nanoTime();
        long elapseSeconds = (endTime - startTime) / (1000 * 1000);
        System.out.println("getClassesRecursively(\"" + path.toAbsolutePath() + "\") = " + elapseSeconds + " msec");

        return classes;
    }

    public static List<String> getClassFilesRecursively(File directory){
        final List<String> fileNames = new ArrayList<>(256);
        if (directory.isDirectory()) {
            // Add "*.class" files...
            File[] classes = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && file.getName().endsWith(".class");
                }
            });
            if (classes != null) {
                for (File file : classes)
                    fileNames.add(file.getAbsolutePath());
            }

            // Add "*class" from subdirectories...
            File[] subdirectories = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            if (subdirectories != null) {
                for (File subdirectory : subdirectories)
                    fileNames.addAll(getClassFilesRecursively(subdirectory));
            }
        }
        else {
            if (directory.getName().endsWith("*.class")) {
                fileNames.add(String.valueOf(directory));
            }
        }

        return fileNames;
    }
}
