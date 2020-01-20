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
package com.fluxtion.articles;

import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.partition.Partitioner;
import com.fluxtion.api.time.Tick;
import com.fluxtion.articles.audit.Position;
import com.fluxtion.articles.audit.generated.reconcile.PositionReconciliator;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class ReconcileTest {
    
     @Test
     public void testSimple() {
         PositionReconciliator reconciller = new PositionReconciliator();
         reconciller.init();
//         reconciller.handleEvent(new EventLogControlEvent(EventLogControlEvent.LogLevel.TRACE));
         
         reconciller.handleEvent(new Position("core", "client_XXX", 12, 45, "EUR", "USD"));
         reconciller.handleEvent(new Position("colo", "client_YYY", 12, 45, "EUR", "USD"));
         reconciller.handleEvent(new Position("core", "client_PKH", 3, 40, "EUR", "USD"));
         reconciller.handleEvent(new Position("core", "hedging_PKH", 3, 40, "EUR", "USD"));
     }
     @Test
     public void testPartition() {
         
         Partitioner<PositionReconciliator> partitioner = new Partitioner(PositionReconciliator::new);
         partitioner.partition(Position::getCcyPair);
//         reconciller.handleEvent(new EventLogControlEvent(EventLogControlEvent.LogLevel.TRACE));
         
         partitioner.onEvent(new Position("core", "client_XXX", 12, 45, "EUR", "USD"));
         partitioner.onEvent(new Position("colo", "client_YYY", 12, 45, "EUR", "USD"));
         partitioner.onEvent(new Position("core", "client_PKH", 3, 40, "EUR", "USD"));
         partitioner.onEvent(new Position("core", "client_PKH", 100, -10, "EUR", "JPY"));
         partitioner.onEvent(new Position("core", "client_PKH", 10, 10, "EUR", "JPY"));
         partitioner.onEvent(new Position("core", "client_PKH", 30, 4, "EUR", "USD"));
         partitioner.onEvent(new Tick());
         
     }
}
