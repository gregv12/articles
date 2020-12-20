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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import lombok.extern.slf4j.Slf4j;
import static net.openhft.compiler.CompilerUtils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("StaticNonFinalField")
@Slf4j
public class CachedCompiler implements Closeable {

    private static final PrintWriter DEFAULT_WRITER = new PrintWriter(System.err);
    private final Map<ClassLoader, Map<String, Class>> loadedClassesMap = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<ClassLoader, MyClassLoader> cl2MyCl = Collections.synchronizedMap(new WeakHashMap<>());
    private final Map<ClassLoader, MyJavaFileManager> fileManagerMap = Collections.synchronizedMap(new WeakHashMap<>());
    @Nullable
    private final File sourceDir;
    @Nullable
    private final File classDir;
    private final Map<String, JavaFileObject> javaFileObjects;

    public CachedCompiler(@Nullable File sourceDir, @Nullable File classDir) {
        this.javaFileObjects = new HashMap<>();
        this.sourceDir = sourceDir;
        this.classDir = classDir;
        log.debug("sourceDir:{} classDir:{}", sourceDir, classDir);
    }

    public void close() {
        try {
            for (MyJavaFileManager fileManager : fileManagerMap.values()) {
                fileManager.close();
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Class loadFromJava(@NotNull String className, @NotNull String javaCode) throws ClassNotFoundException {
        return loadFromJava(getClass().getClassLoader(), className, javaCode, DEFAULT_WRITER);
    }

    public Class loadFromJava(@NotNull ClassLoader classLoader,
                              @NotNull String className,
                              @NotNull String javaCode) throws ClassNotFoundException {
        return loadFromJava(classLoader, className, javaCode, DEFAULT_WRITER);
    }

    @NotNull
    Map<String, byte[]> compileFromJava(@NotNull String className, @NotNull String javaCode, MyJavaFileManager fileManager) {
        return compileFromJava(className, javaCode, DEFAULT_WRITER, fileManager);
    }

    @NotNull
    Map<String, byte[]> compileFromJava(@NotNull String className,
                                        @NotNull String javaCode,
                                        final @NotNull PrintWriter writer,
                                        MyJavaFileManager fileManager) {
        Iterable<? extends JavaFileObject> compilationUnits;
        log.debug("start compileFromJava");
        if (sourceDir != null) {
            String filename = className.replaceAll("\\.", '\\' + File.separator) + ".java";
            File file = new File(sourceDir, filename);
            writeText(file, javaCode);
            compilationUnits = s_compiler.getStandardFileManager(null, null, null).getJavaFileObjects(file);
        } else {
            javaFileObjects.put(className, new JavaSourceFromString(className, javaCode));
            compilationUnits = javaFileObjects.values();
        }
        // reuse the same file manager to allow caching of jar files
        
        log.debug("submit compiler task compileFromJava");
        boolean ok = s_compiler.getTask(writer, fileManager, new DiagnosticListener<JavaFileObject>() {
            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    writer.println(diagnostic);
                }
            }
        }, null, null, compilationUnits).call();
        log.debug("completed compiler task compileFromJava");
        Map<String, byte[]> result = fileManager.getAllBuffers();
        if (!ok) {
            // compilation error, so we want to exclude this file from future compilation passes
            if (sourceDir == null)
                javaFileObjects.remove(className);

            // nothing to return due to compiler error
            return Collections.emptyMap();
        }
        log.debug("end compileFromJava");
        return result;
    }
    
    public Class forName(String className, ClassLoader classLoader){
        Map<String, Class> loadedClasses = loadedClassesMap.get(classLoader);
        return loadedClasses.get(className);
    }

    public Class loadFromJava(@NotNull ClassLoader classLoader,
                              @NotNull String className,
                              @NotNull String javaCode,
                              @Nullable PrintWriter writer) throws ClassNotFoundException {
        log.debug("start loadFromJava");
        Class clazz = null;
        Map<String, Class> loadedClasses;
        synchronized (loadedClassesMap) {
            loadedClasses = loadedClassesMap.get(classLoader);
            if (loadedClasses == null)
                loadedClassesMap.put(classLoader, loadedClasses = new LinkedHashMap<>());
            else
                clazz = loadedClasses.get(className);
        }
        log.debug("loaded classes map");
        PrintWriter printWriter = (writer == null ? DEFAULT_WRITER : writer);
        if (clazz != null)
            return clazz;

        MyJavaFileManager fileManager = fileManagerMap.get(classLoader);
        if (fileManager == null) {
            StandardJavaFileManager standardJavaFileManager = s_compiler.getStandardFileManager(null, null, null);
            fileManager = new MyJavaFileManager(standardJavaFileManager);
            fileManagerMap.put(classLoader, fileManager);
        }
        log.debug("loaded MyJavaFileManager");
        MyClassLoader myClassLoader = cl2MyCl.computeIfAbsent(classLoader, MyClassLoader::new);
        for (Map.Entry<String, byte[]> entry : compileFromJava(className, javaCode, printWriter, fileManager).entrySet()) {
            String className2 = entry.getKey();
            synchronized (loadedClassesMap) {
                if (loadedClasses.containsKey(className2))
                    continue;
            }
            byte[] bytes = entry.getValue();
            if (classDir != null) {
                log.debug("start writing class bytes");
                String filename = className2.replaceAll("\\.", '\\' + File.separator) + ".class";
                boolean changed = writeBytes(new File(classDir, filename), bytes);
                if (changed) {
//                    LOG.info("Updated {} in {}", className2, classDir);
                }
                log.debug("ended writing class bytes");
            }else{
                log.debug("no classes directory - not writing classes classDir:{}", classDir);
            }
            Class clazz2 = CompilerUtils.defineClass(myClassLoader, className2, bytes);
            synchronized (loadedClassesMap) {
                loadedClasses.put(className2, clazz2);
            }
        }
        synchronized (loadedClassesMap) {
            loadedClasses.put(className, clazz = myClassLoader.loadClass(className));
        }
        log.debug("end loadFromJava");
        return clazz;
    }
    
    
}
