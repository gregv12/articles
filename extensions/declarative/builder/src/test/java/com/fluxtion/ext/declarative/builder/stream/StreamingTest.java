/*
 * Copyright (c) 2019, V12 Technology Ltd.
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
package com.fluxtion.ext.declarative.builder.stream;

import com.fluxtion.ext.declarative.api.Wrapper;
import static com.fluxtion.ext.declarative.builder.event.EventSelect.select;
import javafx.util.Pair;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class StreamingTest extends BaseSepInprocessTest {

    @Test
    public void mapPrimitiveFromString() {
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new StreamFunctions()::String2Number, StreamData::getStringValue).id("str2Number");
            in.map(new StreamFunctions()::String2Int, StreamData::getStringValue).id("str2Int");
            in.map(new StreamFunctions()::String2Double, StreamData::getStringValue).id("str2Double");
            in.map(new StreamFunctions()::String2Boolean, StreamData::getStringValue).id("str2Boolean");
        });
        onEvent(new StreamData("23"));
        //int
        Wrapper<Number> valNumber = getField("str2Number");
        assertThat(valNumber.event().intValue(), is(23));
        //Number
        Wrapper<Number> valInt = getField("str2Int");
        assertThat(valInt.event().intValue(), is(23));
        //double
        Wrapper<Number> valDouble = getField("str2Double");
        assertThat(valDouble.event().doubleValue(), is(23.0));
        //double
        onEvent(new StreamData("3.14159"));
        assertThat(valDouble.event().doubleValue(), is(3.14159));
        //boolean
        Wrapper<Boolean> val = getField("str2Boolean");
        onEvent(new StreamData("true"));
        assertThat(val.event(), is(true));
        onEvent(new StreamData("false"));
        assertThat(val.event(), is(false));
    }

    @Test
    public void mapStringFromPrimitive() {
        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new StreamFunctions()::int2String, StreamData::getIntValue).id("int2Str");
            in.map(new StreamFunctions()::double2String, StreamData::getDoubleValue).id("double2Str");
            in.map(new StreamFunctions()::boolean2String, StreamData::isBooleanValue).id("boolean2Str");
            in.map(new StreamFunctions()::number2String, StreamData::getNumberValue).id("number2Str");
        });
        //
        onEvent(new StreamData(23));
        Wrapper<String> valInt = getField("int2Str");
        assertThat(valInt.event(), is("23"));
        //
        onEvent(new StreamData(23.34));
        Wrapper<String> valDouble = getField("double2Str");
        assertThat(valDouble.event(), is("23.34"));
        //
        onEvent(new StreamData(31.34));
        Wrapper<String> valNumber = getField("number2Str");
        assertThat(valNumber.event(), is("31.34"));
        //boolean
        onEvent(new StreamData(true));
        Wrapper<String> valBoolean = getField("boolean2Str");
        assertThat(valBoolean.event(), is("true"));
        onEvent(new StreamData(false));
        valBoolean = getField("boolean2Str");
        assertThat(valBoolean.event(), is("false"));
    }
    
    @Test
    public void mapRef2Ref(){
        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new StreamFunctions()::int2Pair, StreamData::getIntValue).id("pair");
        }); 
        onEvent(new StreamData(89));
        Wrapper<Pair<String, Integer>> valBoolean = getField("pair");
        assertThat(valBoolean.event().getKey(), is("89"));
        assertThat(valBoolean.event().getValue(), is(89));
        
    }
    
    @Test
    public void mapStaticPrimitiveFromString(){
        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(StaticFunctions::statStr2Int, StreamData::getStringValue).id("str2Int");
        }); 
        onEvent(new StreamData("123"));
        Wrapper<Number> valInt = getField("str2Int");
        assertThat(valInt.event().intValue(), is(123));
    }
    
    

}
