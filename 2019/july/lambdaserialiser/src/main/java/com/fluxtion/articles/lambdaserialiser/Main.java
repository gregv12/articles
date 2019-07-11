/*
 * Copyright (C) 2019 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.articles.lambdaserialiser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.function.Function;

/**
 * Example demonstrating serialising a lambda function to a file for a later
 * invocation. Running the application will :
 *
 *
 * <ul>
 * <li>serialise the lambda to a file
 * <li>deserialise the lambda and execute it
 * <li>delete the file
 * <li>fail to deserialise lambda and replace with a new version
 * <li>deserialise the new lambda and execute it producing a different output
 * </ul>
 *
 * Set the {@code
 * serialiseOriginal = false
 * }
 * to demonstrate the application running with whatever the last saved state of
 * the lambda was.
 *
 * @author greg.higgins@v12technology.com
 */
public class Main {

    private static final String ROOT_DIR = "src/main/resources/lambda.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        boolean serialiseOriginal = true;
        if (serialiseOriginal) {
            serialise(s -> "hello " + s, "funcTest-1");
        }
        Function funcDesr = deserialise("funcTest-1");
        System.out.println("funcTest-1: " + funcDesr.apply("serialised lambda"));

        //now fail by deleting file, catch and replace with new lambda 
        Files.delete(new File(ROOT_DIR, "funcTest-1").toPath());
        try {
            funcDesr = deserialise("funcTest-1");
        } catch (IOException | ClassNotFoundException iOException) {
            serialise(s -> "new version of hello " + s, "funcTest-1");
        }
        
        //successfully execute with new lambda
        funcDesr = deserialise("funcTest-1");
        System.out.println("funcTest-1: " + funcDesr.apply("serialised lambda"));
    }

    public static <T extends Function & Serializable> void serialise(T f, String lambdaName) throws IOException {
        final File outDir = new File(ROOT_DIR);
        Files.createDirectories(outDir.toPath());
        OutputStream os = new FileOutputStream(new File(outDir, lambdaName));
        try (ObjectOutputStream ois = new ObjectOutputStream(os)) {
            ois.writeObject(f);
        }
    }

    public static <T, R> Function<T, R> deserialise(String lambdaName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(ROOT_DIR, lambdaName)))) {
            return (Function<T, R>) ois.readObject();
        }
    }

}
