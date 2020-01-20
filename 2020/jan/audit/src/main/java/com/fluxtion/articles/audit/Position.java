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

import com.fluxtion.api.event.Event;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.ext.text.api.annotation.CsvMarshaller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
@CsvMarshaller(packageName = "com.fluxtion.articles.audit.csv.marshallers")
@Disabled
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position extends Event{
    private String source;
    private String tradeType;
    private double base;
    private double terms;
    private String baseCcy;
    private String termsCcy;
    
    public String getCcyPair(){
        return baseCcy + termsCcy;
    }
    
}
