/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dx.command.dexer;

import com.android.dx.command.DxConsole;
import com.android.dx.util.FileUtils;
import com.android.dx.util.StopWatch;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class for the class file translator.
 */
public class Main {

    private final DxContext context;

    public Main(DxContext context) {
        this.context = context;
    }

    /**
     * Run and return a result code.
     * @param arguments the data + parameters for the conversion
     * @return 0 if success &gt; 0 otherwise.
     */
    @SuppressWarnings("unused")
    public static int run(Arguments arguments) {
        return new Main(new DxContext(DxConsole.out, DxConsole.err)).runDx(arguments);
    }

    public int runDx(Arguments arguments) {
        // Init
        context.out.println();
        context.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
        context.out.println("Android DX to D8 adapter");
        context.out.println("Version 1.0-FEB-2023");
        context.out.println();

        // Arguments...
        context.out.println("DX called with:");
        context.out.println(" verbose    = " + arguments.verbose);
        context.out.println(" forceJumbo = " + arguments.forceJumbo);
        context.out.println(" jarOutput  = " + arguments.jarOutput);
        context.out.println(" outName    = " + arguments.outName);
        context.out.println(" fileNames  = " + Arrays.toString(arguments.fileNames));
        context.out.println();

        try {
            // Looking for 'd8.jar'...
            final File dxJarPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            final Path d8JarPath = Paths.get(dxJarPath.getParentFile().getAbsolutePath(), "d8.jar");

            if (Files.notExists(d8JarPath)) {
                context.err.printf("ERROR: %s not found.%n", dxJarPath);
                context.err.println();
                return 1;
            }

            // Strip "classes.dex" from "outName" if present, D8 wants the directory instead...
            String outName = arguments.outName;
            if (outName.endsWith("classes.dex")) {
                outName = outName.replaceAll("classes.dex$", "");
            }

            // Preparing "d8.jar" arguments...
            ArrayList<String> d8Arguments = new ArrayList<>(1024);
            d8Arguments.add("--output");
            d8Arguments.add(outName);

            for (String fileName : arguments.fileNames) {
                final File file = new File(fileName);
                if (file.isDirectory()) {
                    d8Arguments.addAll(FileUtils.getClassesRecursively(file.toPath()));
                }
                else if (file.isFile()) {
                    d8Arguments.add(fileName);
                }
            }

            // Calling "d8.jar"...
            final StopWatch stopWatch = new StopWatch();
            context.out.println();
            context.out.println("D8 called with:");
            context.out.println("  jar       = " + d8JarPath);
            context.out.println("  arguments = " + Arrays.toString(d8Arguments.toArray()));

            URLClassLoader classloader = new URLClassLoader(
                    new URL[] { d8JarPath.toUri().toURL() },
                    this.getClass().getClassLoader()
            );
            Class<?> classToLoad = Class.forName("com.android.tools.r8.D8", true, classloader);
            Method method = classToLoad.getDeclaredMethod("main", String[].class);
            method.invoke(null, new Object[] { d8Arguments.toArray(new String[0]) });

            // Timing
            context.out.println("  elapsed  = " + stopWatch);

            return 0;
        } catch (Exception e) {
            e.printStackTrace(context.err);
            return 1;
        }
    }

    /**
     * Command-line argument parser and access.
     */
    public static class Arguments {

        public final DxContext context;

        /** whether to emit high-level verbose human-oriented output */
        public boolean verbose = false;

        /** {@code null-ok;} output file name for binary file */
        public String outName = null;

        /**
         * whether the binary output is to be a {@code .jar} file
         * instead of a plain {@code .dex}
         */
        public boolean jarOutput = false;

        /** whether to force generation of const-string/jumbo for all indexes,
         *  to allow merges between dex files with many strings. */
        public boolean forceJumbo = false;

        /** {@code non-null} after {@link #parse}; file name arguments */
        public String[] fileNames;

        public Arguments(DxContext context) {
            this.context = context;
        }

        @SuppressWarnings("unused")
        public Arguments() {
            this(new DxContext());
        }
    }
}
