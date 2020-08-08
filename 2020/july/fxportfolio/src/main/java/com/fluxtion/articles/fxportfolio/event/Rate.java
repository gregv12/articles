/*
 * Copyright (C) 2020 V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio.event;

import com.fluxtion.api.event.Event;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.ext.text.api.annotation.ConvertField;
import com.fluxtion.ext.text.api.annotation.CsvMarshaller;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
@Data
@CsvMarshaller(packageName = "com.fluxtion.articles.fxportfolio.csvmarshaller")
public class Rate implements Event {

    @ConvertField("com.fluxtion.articles.fxportfolio.shared.CcyPair#ccyPairFromCharSeq")
    public CcyPair ccypair;
    public double value;

    public Rate(CcyPair ccypair, double value) {
        this.ccypair = ccypair;
        this.value = value;
    }

    public Rate() {
    }

    @Override
    public String filterString() {
        return ccypair == null ? "" : ccypair.name;
    }

}
