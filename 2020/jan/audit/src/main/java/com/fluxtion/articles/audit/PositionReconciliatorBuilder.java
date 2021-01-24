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
package com.fluxtion.articles.audit;

import com.fluxtion.api.audit.EventLogControlEvent.LogLevel;
import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.builder.factory.FilterBuilder.filter;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.delta;
import static com.fluxtion.ext.streaming.builder.log.LogBuilder.*;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class PositionReconciliatorBuilder {

    @SepBuilder(
            name = "PositionReconciliator",
            packageName = "com.fluxtion.articles.audit.generated.reconcile"
    )
    public void buildCalc(SEPConfig cfg) {
        Wrapper<Position> corePos = filter(Position::getSource, ("core")::equals).id("corePosition");
//        Wrapper<Position> coloPos = filter(Position::getSource, "colo"::equals).id("coloPosition"); 
        Log("-> received [{}]", Position.class);
        Trade outTrade = new Trade();
        corePos.push(Position::getBaseCcy, outTrade::setBaseCcy);
        corePos.push(Position::getTermsCcy, outTrade::setTermsCcy);
        corePos.push(Position::getTradeType, outTrade::setTradeType);
        corePos.map(delta(), Position::getBase).push(outTrade::setBase).id("baseQuantiy");
        corePos.map(delta(), Position::getTerms).push(outTrade::setTerms).id("termsQuantiy");
        Wrapper<Trade> clientTrade = filter(outTrade, Trade::getTradeType, (s) -> s.startsWith("client") )
                .id("clientTrades")
                .push(new HouseTradePublisher()::publishHouseTrade);
        Log("<- pushing client trade to colo [{}]", clientTrade);
        cfg.addEventAudit(LogLevel.INFO);
        cfg.addNode(new MyClockHolder(cfg.clock()));
    }

}
