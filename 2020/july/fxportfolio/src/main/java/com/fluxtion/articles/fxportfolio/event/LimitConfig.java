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
package com.fluxtion.articles.fxportfolio.event;

import com.fluxtion.api.event.Event;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.ext.text.api.annotation.CsvMarshaller;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
@CsvMarshaller(packageName = "com.fluxtion.articles.fxportfolio.csvmarshaller")
public class LimitConfig implements Event{
    
    private Ccy ccy;
    private int limit;
    private int target;

    public LimitConfig() {
    }

    public LimitConfig(Ccy ccy, int limit, int target) {
        this.ccy = ccy;
        this.limit = limit;
        this.target = target;
    }

    @Override
    public String filterString() {
        return ccy.name();
    }

    public Ccy getCcy() {
        return ccy;
    }

    public int getLimit() {
        return limit;
    }

    public int getTarget() {
        return target;
    }

    public void setCcy(Ccy ccy) {
        this.ccy = ccy;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTarget(int target) {
        this.target = target;
    }
    
    @Override
    public String toString() {
        return "LimitConfig: {" + "ccy:" + ccy + ", limit:" + limit + ", target:" + target + '}';
    }
    
}
