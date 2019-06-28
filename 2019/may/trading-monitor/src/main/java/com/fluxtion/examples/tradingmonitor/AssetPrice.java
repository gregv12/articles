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
package com.fluxtion.examples.tradingmonitor;

import com.fluxtion.api.event.Event;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.ext.text.api.annotation.CsvMarshaller;

/**
 * An {@link Event} that represents a market price event for an asset. This is a
 * simplified description for the purposes of the example.<p>
 * 
 * The CsvMarshaller annotation instructs Fluxtion to generate a CSV parser for
 * this class. Uncomment the Disabled annotation to disable FLuxtion generation
 * for this class.
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
@CsvMarshaller(
        packageName = "com.fluxtion.examples.tradingmonitor.generated"
)
@Disabled
public class AssetPrice extends Event {

    private CharSequence symbol;
    private double price;

    public CharSequence getSymbol() {
        return symbol;
    }

    public void setSymbol(CharSequence symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AssetPrice{" + "symbol=" + symbol + ", price=" + price + '}';
    }

}
