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
package com.fluxtion.articles.lambdaserialiser.fluxtion;

import com.fluxtion.api.event.Event;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.count;
import lombok.Data;

/**
 * Class demonstrating the use of design time lambdas in generated code. The
 * filter expression is expressed as a lambda:
 * <pre>
 * {@code
 *  select(TrafficCount.class)
 *    .filter(t -> t.getCity().toLowerCase().equals("london"))
 * }
 * </pre>
 *
 * @author V12 Technology Ltd.
 */
public class ProcessBuilder {

    @SepBuilder(name = "TrafficProcessor", packageName = "com.fluxtion.articles.lambdaserialiser.fluxtion.generated")
    public void build(SEPConfig cfg) {
        select(TrafficCount.class)
                .filter(t -> t.getCity().toLowerCase().equals("london"))
                .map(count());
    }

    @Data
    public static class TrafficCount extends Event {

        private String city;
        private long time;
    }

}
