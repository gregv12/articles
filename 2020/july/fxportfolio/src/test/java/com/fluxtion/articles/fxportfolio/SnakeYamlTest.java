/*
 * Copyright (c) 2020, V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class SnakeYamlTest {
    
    
    @Test
    public void testMaps(){
        Map<String, List<String>> data = new HashMap<>();
        data.put("EUR", List.of("EURGBP", "EURUSD"));
        data.put("USD", List.of("GBPUSD"));
        Yaml yaml = new Yaml();
        System.out.println(yaml.dump(data));
        System.out.println(yaml.dumpAsMap(data));
        
        
        String s = "EUR: [EURGBP, EURUSD]\n"
                + "USD: [GBPUSD]";

        data = (Map<String, List<String>>) yaml.load(s);
        System.out.println(data);
    }
}
