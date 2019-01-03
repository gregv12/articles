/* 
 * Copyright (C) 2018 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.builder.generation;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.openhft.compiler.CachedCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Context for the generated output of the SEP.
 *
 * @author Greg Higgins
 */
public class GenerationContext {

    
    public static GenerationContext SINGLETON;
    private static final AtomicInteger COUNT = new AtomicInteger();
    private final Map<? super Object, Map> cacheMap;
    
    /**
     * A global counter, can be used for generating unique class names.
     * @return next id.
     */
    public static int nextId(){
        return COUNT.getAndIncrement();
    }
    
    public static void setupStaticContext(String packageName, String className, File outputDirectory, File resourcesRootDirectory) {
        setupStaticContext(packageName, className, outputDirectory, resourcesRootDirectory, false);
    }
    
    public static void setupStaticContext(String packageName, String className, File outputDirectory, File resourcesRootDirectory, boolean createResourceDirectory) {
        SINGLETON = new GenerationContext(packageName, className, outputDirectory, resourcesRootDirectory);
        SINGLETON.createDirectories();
        if(createResourceDirectory)
            SINGLETON.createResourceDirectory();
    }
    
    public static void setupStaticContext(ClassLoader classLoader, String packageName, String className, File outputDirectory, File resourcesRootDirectory, boolean createResourceDirectory, File buildOutputDirectory, boolean createBuildOutputDirectory) {
        SINGLETON = new GenerationContext(
                classLoader,
                packageName, 
                className, 
                outputDirectory, 
                resourcesRootDirectory,
                buildOutputDirectory,
                null
        );
        SINGLETON.createDirectories();
        if(createResourceDirectory)
            SINGLETON.createResourceDirectory();
        if(createBuildOutputDirectory && buildOutputDirectory!=null){
            buildOutputDirectory.mkdirs();
        }
    }
    
    public static void setupStaticContext(ClassLoader classLoader, 
            String packageName, 
            String className, 
            File outputDirectory, 
            File resourcesRootDirectory, 
            boolean createResourceDirectory, 
            File buildOutputDirectory, 
            boolean createBuildOutputDirectory,
            CachedCompiler cachedCompiler) {
        SINGLETON = new GenerationContext(
                classLoader,
                packageName, 
                className, 
                outputDirectory, 
                resourcesRootDirectory,
                buildOutputDirectory,
                cachedCompiler
        );
        SINGLETON.createDirectories();
        if(createResourceDirectory)
            SINGLETON.createResourceDirectory();
        if(createBuildOutputDirectory && buildOutputDirectory!=null){
            buildOutputDirectory.mkdirs();
        }
    }
    
    
    /**
     * Map representing the name of the actual node class generated at SEP
     * processing stage. Allowing a generic proxy class to be used in the SEP
     * model processing phase and then replaced with the actual class reference
     * during the code generation phase. The real class name is only known after
     * the proxy has been generated.
     *
     */
    private final Map<Object, String> proxyClassMap = new HashMap<>();
    
    private final List<?> nodeList = new ArrayList<>();

    private final ClassLoader classLoader;
    
    /**
     * Output package for the generated file, used where relevant
     */
    private final String packageName;

    /**
     * Class name for the generated output file
     */
    private final String className;

    /**
     * the root output directory for the code generation
     */
    private final File srcRootDirectory;
    
    /**
     * The package directory = outputDirectory + packageName
     */
    private File srcPackageDirectory;
    
     /**
     * the output directory for the code generation
     */
    public File resourcesRootDirectory;
    public File resourcesOutputDirectory;
    private final CachedCompiler javaCompiler;

    public GenerationContext(String packageName, String className, File outputDirectory, File resourcesRootDirectory) {
        this(packageName, className, outputDirectory, resourcesRootDirectory, null);
    }

    private GenerationContext(String packageName, String className, File outputDirectory, File resourcesRootDirectory, File buildOutputDirectory) {
        this.packageName = packageName;
        this.className = className;
        this.srcRootDirectory = outputDirectory;
        this.resourcesRootDirectory = resourcesRootDirectory;
        this.classLoader = this.getClass().getClassLoader();
        javaCompiler = new CachedCompiler(null, buildOutputDirectory);
        cacheMap = new HashMap<>();
    }

    private GenerationContext(ClassLoader classLoasder, String packageName, String className, File outputDirectory, File resourcesRootDirectory, File buildOutputDirectory, CachedCompiler cachedCompiler) {
        this.packageName = packageName;
        this.className = className;
        this.srcRootDirectory = outputDirectory;
        this.resourcesRootDirectory = resourcesRootDirectory;
        this.classLoader = classLoasder;
        if(cachedCompiler == null){
            javaCompiler = new CachedCompiler(null, buildOutputDirectory);
        }else{
            javaCompiler = cachedCompiler;
        }
        cacheMap = new HashMap<>();
    }
    
    private void createDirectories() {
        srcPackageDirectory = new File(GenerationContext.SINGLETON.srcRootDirectory, packageName.replace(".", "/"));
        srcPackageDirectory.mkdirs();
    }
    
    public void createResourceDirectory(){
        resourcesOutputDirectory = new File(resourcesRootDirectory, packageName.replace(".", "/"));
        resourcesOutputDirectory.mkdirs();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSepClassName() {
        return className;
    }

    public File getSourceRootDirectory() {
        return srcRootDirectory;
    }

    public Map getProxyClassMap() {
        return proxyClassMap;
    }

    public File getPackageDirectory() {
        return srcPackageDirectory;
    }

    public File getResourcesRootDirectory() {
        return resourcesRootDirectory;
    }

    public File getResourcesOutputDirectory() {
//        if(resourcesOutputDirectory==null){
//            createResourceDirectory();
//        }
        return resourcesOutputDirectory;
    }

    public List getNodeList() {
        return nodeList;
    }
    
    public <T> T addOrUseExistingNode(T node){
        if(getNodeList().contains(node)){
            return (T) getNodeList().get(getNodeList().indexOf(node));
        }
        getNodeList().add(node);
        return node;
    }

    public CachedCompiler getJavaCompiler() {
        return javaCompiler;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /**
     * a cache that is tied to this generation context instance. A new Map
     * will be created for each unique cache key.
     * 
     * @param <K> The key type of the cache map
     * @param <V> The value type of the cache map
     * @param key the cache key
     * @return the newly created map
     */
    public  <K, V> Map<K, V> getCache(Object key){
        return cacheMap.computeIfAbsent(key, (k) ->  new HashMap());
    }
    
    /**
     * removes a cache map from this instance by key.
     * @param <K> The key type of the cache map
     * @param <V> The value type of the cache map
     * @param key the cache key
     * @return The mapping of the map removed or null if no mapping
     */
    public <K, V> Map<K, V> removeCache(Object key){
        return cacheMap.remove(key);
    }
    
    public static String readText(@NotNull String resourceName) throws IOException {
        StringWriter sw = new StringWriter();
        Reader isr = new InputStreamReader(getInputStream(resourceName), UTF_8);
        try {
            char[] chars = new char[8 * 1024];
            int len;
            while ((len = isr.read(chars)) > 0) {
                sw.write(chars, 0, len);
            }
        } finally {
            close(isr);
        }
        return sw.toString();
    }

    private static void close(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.err.println("Failed to close " +  closeable +  e.getMessage());
            }
        }
    }

    private static InputStream getInputStream(@NotNull String filename) throws FileNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = contextClassLoader.getResourceAsStream(filename);
        if (is != null) {
            return is;
        }
        InputStream is2 = contextClassLoader.getResourceAsStream('/' + filename);
        if (is2 != null) {
            return is2;
        }
        return new FileInputStream(filename);
    }
    
}