/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.compiler;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import junit.framework.TestCase;
import net.openhft.compiler.eg.components.Foo;
import org.junit.Test;

public class CompilerTest extends TestCase {
    static final File parent;
    private static final int RUNS = 1000 * 1000;

    static {
        File parent2 = new File("essence-file");
        if (parent2.exists()) {
            parent = parent2;

        } else {
            parent = new File(".");
        }
    }

    public void test_fromFile()
            throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class clazz = CompilerUtils.loadFromResource("net.openhft.compiler.eg.FooBarTee2", "net/openhft/compiler/eg/FooBarTee2.jcf");
        // turn off System.out
        PrintStream out = System.out;
        try {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            }));
            final Constructor stringConstructor = clazz.getConstructor(String.class);
            long start = 0;
            for (int i = -RUNS / 10; i < RUNS; i++) {
                if (i == 0) start = System.nanoTime();

                Object fooBarTee2 = stringConstructor.newInstance(getName());
                Foo foo = (Foo) clazz.getDeclaredField("foo").get(fooBarTee2);
                assertNotNull(foo);
                assertEquals("load java class from file.", foo.s);
            }
            long time = System.nanoTime() - start;
            out.printf("Build build small container %,d ns.%n", time / RUNS);
        } finally {
            System.setOut(out);
        }
    }

    public void test_settingPrintStreamWithCompilerErrors() throws Exception {
        final AtomicBoolean usedSysOut = new AtomicBoolean(false);
        final AtomicBoolean usedSysErr = new AtomicBoolean(false);

        final PrintStream out = System.out;
        final PrintStream err = System.err;
        final StringWriter writer = new StringWriter();

        try {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    usedSysOut.set(true);
                }
            }));
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    usedSysErr.set(true);
                }
            }));

            CompilerUtils.CACHED_COMPILER.loadFromJava(
                    getClass().getClassLoader(), "TestClass", "clazz TestClass {}",
                    new PrintWriter(writer));
            fail("Should have failed to compile");
        } catch (ClassNotFoundException e) {
            // expected
        } finally {
            System.setOut(out);
            System.setErr(err);
        }

//        assertFalse(usedSysOut.get());
//        assertFalse(usedSysErr.get());

        List<String> expectedInErrorFromCompiler = Arrays.asList(
                "TestClass.java:1: error", "clazz TestClass {}");

        for (String expectedError : expectedInErrorFromCompiler) {
            String errorMessage = String.format("Does not contain expected '%s' in:\n%s", expectedError, writer.toString());
            assertTrue(errorMessage, writer.toString().contains(expectedError));
        }
    }

    public void test_settingPrintStreamWithNoErrors() throws Exception {
        final AtomicBoolean usedSysOut = new AtomicBoolean(false);
        final AtomicBoolean usedSysErr = new AtomicBoolean(false);

        final PrintStream out = System.out;
        final PrintStream err = System.err;
        final StringWriter writer = new StringWriter();

        try {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    usedSysOut.set(true);
                }
            }));
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    usedSysErr.set(true);
                }
            }));

            CompilerUtils.CACHED_COMPILER.loadFromJava(
                    getClass().getClassLoader(), "TestClass", "class TestClass {}",
                    new PrintWriter(writer));
        } finally {
            System.setOut(out);
            System.setErr(err);
        }

        assertFalse(usedSysOut.get());
        assertFalse(usedSysErr.get());
        assertEquals("", writer.toString());
    }

    public void test_settingPrintStreamWithWarnings() throws Exception {
        final AtomicBoolean usedSysOut = new AtomicBoolean(false);
        final AtomicBoolean usedSysErr = new AtomicBoolean(false);

        final PrintStream out = System.out;
        final PrintStream err = System.err;
        final StringWriter writer = new StringWriter();

        try {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    usedSysOut.set(true);
                }
            }));
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    usedSysErr.set(true);
                }
            }));

            CompilerUtils.CACHED_COMPILER.loadFromJava(
                    getClass().getClassLoader(), "TestClass",
                    // definition with a mandatory warning
                    "class TestClass { int i = new Date().getDay(); }",
                    new PrintWriter(writer));
        } finally {
            System.setOut(out);
            System.setErr(err);
        }

        assertFalse(usedSysOut.get());
        assertFalse(usedSysErr.get());
        assertEquals("", writer.toString());
    }

    public void test_compilerErrorsDoNotBreakNextCompilations() throws Exception {
        // quieten the compiler output
        PrintWriter quietWriter = new PrintWriter(new StringWriter());

        // cause a compiler error
        try {
            CompilerUtils.CACHED_COMPILER.loadFromJava(
                    getClass().getClassLoader(), "X", "clazz X {}", quietWriter);
            fail("Should have failed to compile");
        } catch (ClassNotFoundException e) {
            // expected
        }

        // ensure next class can be compiled and used
        Class testClass = CompilerUtils.CACHED_COMPILER.loadFromJava(
                getClass().getClassLoader(), "S", "class S {" +
                        "public static final String s = \"ok\";}");

        Callable callable = (Callable)
                CompilerUtils.CACHED_COMPILER.loadFromJava(
                        getClass().getClassLoader(), "OtherClass",
                        "import java.util.concurrent.Callable; " +
                                "public class OtherClass implements Callable<String> {" +
                                "public String call() { return S.s; }}")
                        .newInstance();

        assertEquals("S", testClass.getName());
        assertEquals("ok", callable.call());
    }

    @Test
    public void testNewCompiler() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (int i = 1; i <= 3; i++) {
            ClassLoader classLoader = new ClassLoader() {};
            CachedCompiler cc = new CachedCompiler(null, null);
            Class a = cc.loadFromJava(classLoader, "A", "public class A { static int i = " + i + "; }");
            Class b = cc.loadFromJava(classLoader, "B", "public class B implements net.openhft.compiler.MyIntSupplier { public int get() { return A.i; } }");
            MyIntSupplier bi = (MyIntSupplier) b.newInstance();
            assertEquals(i, bi.get());
        }
    }
}

