/* 
 * Copyright (c) 2019, V12 Technology Ltd.
 * All rights reserved.
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
package com.fluxtion.generator.compiler;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.builder.generation.GenerationContext;
import static com.fluxtion.builder.generation.GenerationContext.SINGLETON;
import com.fluxtion.builder.node.SEPConfig;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Consumer;

/**
 * Generates and compiles a SEP for use by a caller in the same process. The
 * compilation is invoked programmatically removing the need to execute the
 * Fluxtion event stream compiler as an external process.<br><br>
 *
 * To generate a SEP the caller invokes one of the static compileSep methods. An
 * instance of {@link SEPConfig} is passed to the consumer to control the graph
 * construction, such as adding nodes and defining scopes or identifiers. Simple
 * example adding a single node:<br><br>
 *
 * {@code  sepTestInstance((c) -> c.addNode(new MyHandler(), "handler"), "com.fluxtion.examples.inprocess", "GenNode_1");}
 * <br><br>
 *
 * Optionally creates an instance of the compiled StaticEventProcessor with or
 * without calling the init method using one of {@link #sepInstance(Consumer, String, String, String, String, boolean)
 * }.<br><br>
 *
 * <h2>>This is an experimental feature that needs to tested carefully. The
 * class loading for SEP generation was originally designed to be out of process
 * so there may be issues.</h2>
 *
 * @author V12 Technology Ltd.
 */
public class InprocessSepCompiler {

    public static final String JAVA_TESTGEN_DIR = "target/generated-test-sources/fluxtion/";
    public static final String JAVA_GEN_DIR = "target/generated-sources/fluxtion/";
    public static final String JAVA_SRC_DIR = "src/main/java/";
    public static final String JAVA_TEST_SRC_DIR = "src/test/java/";

    public static final String RESOURCE_TEST_DIR = "target/generated-test-sources/resources/";
    public static final String RESOURCE_DIR = "src/main/resources/";

    public enum DirOptions {
        TEST_DIR_OUTPUT,
        JAVA_GENDIR_OUTPUT,
        JAVA_SRCDIR_OUTPUT
    }

    public enum InitOptions {
        INIT,
        NO_INIT
    }

    public static StaticEventProcessor sepInstance(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName, DirOptions dirOptions, InitOptions initOptions) throws InstantiationException, IllegalAccessException, Exception {
        String genDir = JAVA_GEN_DIR;
        String resDir = RESOURCE_DIR;
        switch (dirOptions) {
            case JAVA_SRCDIR_OUTPUT:
                genDir = JAVA_SRC_DIR;
                resDir = RESOURCE_DIR;
                break;
            case TEST_DIR_OUTPUT:
                genDir = JAVA_TESTGEN_DIR;
                resDir = RESOURCE_TEST_DIR;
        }
        boolean init = initOptions == InitOptions.INIT;
        return sepInstance(cfgBuilder, pckg, sepName, genDir, resDir, init);
    }

    public static StaticEventProcessor sepInstance(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws InstantiationException, IllegalAccessException, Exception {
        return sepInstance(cfgBuilder, pckg, sepName, JAVA_GEN_DIR, RESOURCE_DIR, true);
    }

    public static StaticEventProcessor sepTestInstance(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws InstantiationException, IllegalAccessException, Exception {
        return sepInstance(cfgBuilder, pckg, sepName, JAVA_TESTGEN_DIR, RESOURCE_TEST_DIR, true);
    }

    public static StaticEventProcessor sepInstanceNoInit(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws InstantiationException, IllegalAccessException, Exception {
        return sepInstance(cfgBuilder, pckg, sepName, JAVA_GEN_DIR, RESOURCE_DIR, false);
    }

    public static StaticEventProcessor sepTestInstanceNoInit(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws InstantiationException, IllegalAccessException, Exception {
        return sepInstance(cfgBuilder, pckg, sepName, JAVA_TESTGEN_DIR, RESOURCE_TEST_DIR, false);
    }

    /**
     * Build a static event processor using the supplied consumer to populate
     * the SEPConfig. Will always build a new processor, supplying a newly
     * created instance of the class to the caller.
     * 
     * <p>
     * Set the system property fluxtion.cacheDirectory and fluxtion will create the 
     * following sub-directories:
     * <ul>
     * <li>classes - the compiled classes
     * <li>source - generated source files the classes are compiled from
     * <li>resources - any other resources generated by fluxtion, suchas meta-data
     * </ul>
     *
     * @param name The name of the generated static event processor
     * @param pkg The package name of the generated static event processor
     * @param builder The Consumer that populates the SEPConfig
     * @return An instance of the newly generated static event processor
     * @throws Exception
     */
    public static StaticEventProcessor build(String name, String pkg, Consumer<SEPConfig> builder) throws Exception {
        String dir = System.getProperty("fluxtion.cacheDirectory");
        if (dir != null) {
            System.setProperty("fluxtion.build.outputdirectory", dir + "/classes/");
            return InprocessSepCompiler.sepInstance(builder, pkg, name, dir + "/source/", dir + "/resources/", true);
        } 
        return InprocessSepCompiler.sepInstance(builder, pkg, name);
    }

    /**
     * Returns an instance of a static event processor to the caller. Will only
     * build a new processor if a class cannot be found on the classpath that
     * matches the fqn name of the processor. Will generate a static event
     * processor using the supplied consumer to populate the SEPConfig if an
     * existing class cannot be found.
     * 
     * <p>
     * Set the system property fluxtion.cacheDirectory and fluxtion will create the 
     * following sub-directories:
     * <ul>
     * <li>classes - the compiled classes
     * <li>source - generated source files the classes are compiled from
     * <li>resources - any other resources generated by fluxtion, suchas meta-data
     * </ul>
     * 
     * @param name The name of the generated static event processor
     * @param pkg The package name of the generated static event processor
     * @param builder The Consumer that populates the SEPConfig
     * @return An instance of the newly generated static event processor
     * @throws Exception
     */
    public static StaticEventProcessor reuseOrBuild(String name, String pkg, Consumer<SEPConfig> builder) throws Exception {
        StaticEventProcessor processor;
        String dir = System.getProperty("fluxtion.cacheDirectory");
        Class<? extends StaticEventProcessor> processorClass;
        try {
            if (dir != null) {
                final URL classesDir = new File(dir + "/classes/").toURI().toURL();
                final URL reosurcesDir = new File(dir + "/resources/").toURI().toURL();
                URLClassLoader ucl = new URLClassLoader(new URL[]{classesDir, reosurcesDir});
                processorClass = Class.forName(pkg + "." + name, true, ucl).asSubclass(StaticEventProcessor.class);
            } else {
                processorClass = Class.forName(pkg + "." + name).asSubclass(StaticEventProcessor.class);
            }
            processor = processorClass.getDeclaredConstructor().newInstance();
            if (processor instanceof Lifecycle) {
                Lifecycle lifecycle = (Lifecycle) processor;
                lifecycle.init();
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            if (dir != null) {
                System.setProperty("fluxtion.build.outputdirectory", dir + "/classes/");
                processor = InprocessSepCompiler.sepInstance(builder, pkg, name, dir + "/source/", dir + "/resources/", true);
            } else {
                processor = InprocessSepCompiler.sepInstance(builder, pkg, name);
            }
        }
        return processor;
    }

    /**
     * Compiles and instantiates a SEP described with the provided
     * {@link SEPConfig}, optionally initialising the SEP instance. See {@link #compileSep(Consumer, String, String, String, String)
     * } for a description of compilation.
     *
     * @param cfgBuilder - A client consumer to buld sep using the provided
     * @param pckg - output package of the generated class
     * @param sepName - output class name of the generated SEP
     * @param srcGenDir - output directory for generated SEP source files
     * @param resGenDir - output directory for generated resources
     * @param initialise - if true call init method on SEP instance
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws Exception
     */
    public static StaticEventProcessor sepInstance(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName, String srcGenDir, String resGenDir, boolean initialise) throws InstantiationException, IllegalAccessException, Exception {
        Class<StaticEventProcessor> sepClass = compileSep(cfgBuilder, pckg, sepName, srcGenDir, resGenDir);
        StaticEventProcessor sep = sepClass.newInstance();
        if (initialise) {
            if (sep instanceof Lifecycle) {
                ((Lifecycle) sep).init();
            }
        }
        return sep;
    }

    public static Class<StaticEventProcessor> compileSep(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws IOException, InstantiationException, IllegalAccessException, Exception {
        return compileSep(cfgBuilder, pckg, sepName, JAVA_GEN_DIR, RESOURCE_DIR);
    }

    public static Class<StaticEventProcessor> CompileTestSep(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName) throws IOException, InstantiationException, IllegalAccessException, Exception {
        return compileSep(cfgBuilder, pckg, sepName, JAVA_TESTGEN_DIR, RESOURCE_TEST_DIR);
    }

    /**
     * Compiles a SEP in the current process of the caller. The provided
     * {@link SEPConfig} is used by the Fluxtion event stream compiler to build
     * the SEP.
     *
     * @param cfgBuilder - A client consumer to buld sep using the provided
     * @param pckg - output package of the generated class
     * @param sepName - output class name of the generated SEP
     * @param srcGenDir - output directory for generated SEP source files
     * @param resGenDir - output directory for generated resources
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws Exception
     */
    private static Class<StaticEventProcessor> compileSep(Consumer<SEPConfig> cfgBuilder, String pckg, String sepName, String srcGenDir, String resGenDir) throws IOException, InstantiationException, IllegalAccessException, Exception {
        SepCompiler compiler = new SepCompiler();
        final SepCompilerConfig compilerCfg = getSepCompileConfig(pckg, sepName, srcGenDir, resGenDir);
        return (Class<StaticEventProcessor>) compiler.compile(compilerCfg, new InProcessSepConfig(cfgBuilder));
    }

    private static class InProcessSepConfig extends SEPConfig {

        private final Consumer<SEPConfig> cfg;

        public InProcessSepConfig(Consumer<SEPConfig> cfg) {
            this.cfg = cfg;
        }

        @Override
        public void buildConfig() {
            cfg.accept(this);
        }

    }

    public static SepCompilerConfig getSepCompileConfig(String packageName, String className, String srcGenDir, String resGenDir) throws IOException {
        File outputDir = new File(srcGenDir);
        File resourcesDir = new File(resGenDir);
        GenerationContext.setupStaticContext(packageName, className, outputDir, resourcesDir);
        SepCompilerConfig cfg = new SepCompilerConfig();
        cfg.setOutputDirectory(SINGLETON.getSourceRootDirectory().getCanonicalPath());
        cfg.setResourcesOutputDirectory(SINGLETON.getResourcesRootDirectory().getCanonicalPath());
        cfg.setPackageName(packageName);
        cfg.setClassName(className);
        cfg.setCachedCompiler(SINGLETON.getJavaCompiler());
        cfg.setConfigClass(InProcessSepConfig.class.getCanonicalName());
        return cfg;
    }
}
