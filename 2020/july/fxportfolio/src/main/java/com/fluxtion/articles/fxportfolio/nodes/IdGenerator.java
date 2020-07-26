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
package com.fluxtion.articles.fxportfolio.nodes;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.event.Signal;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.ORDER_ID;
import java.util.Queue;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class IdGenerator {
    
    private Queue<String> idQueue;
    
    @EventHandler(filterString = ORDER_ID, propagate = false)
    public void nextId(Signal<Queue<String>> nextIdSignal){
        idQueue = nextIdSignal.getValue();
    }

    public String getNextId() {
        return idQueue.remove();
    }

    
}
