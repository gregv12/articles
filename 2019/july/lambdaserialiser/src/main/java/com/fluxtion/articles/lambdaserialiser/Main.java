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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Function;

/**
 *
 * @author V12 Technology Ltd.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (false) {
            serialise(s -> "hello - " + s, "func1");
        }
        System.out.println(deserialise("func1").apply("Greg"));
        //rewrite func-1
        serialise(s -> "goodbye - " + s, "func1");
        System.out.println(deserialise("func1").apply("Greg"));
    }

    public static <F extends Function & Serializable> void serialise(F f, String name) throws Exception {
        try (var oos = new ObjectOutputStream(new FileOutputStream(new File(name)))) {
            oos.writeObject(f);
        }
    }

    public static <T, R, F extends Function<T, R>> F deserialise(String name) throws Exception {
        try (var ois = new ObjectInputStream(new FileInputStream(name))) {
            return (F) ois.readObject();
        }
    }

}
