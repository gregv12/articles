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

import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.util.Pair;
import static com.fluxtion.ext.streaming.builder.factory.EventSelect.select;
import lombok.Data;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class StreamingMapTest extends StreamInprocessTest {

    @Test
    public void mapPrimitiveFromString() {
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new MapFunctions()::String2Number, StreamData::getStringValue).id("str2Number");
            in.map(new MapFunctions()::String2Int, StreamData::getStringValue).id("str2Int");
            in.map(new MapFunctions()::String2Double, StreamData::getStringValue).id("str2Double");
            in.map(new MapFunctions()::String2Boolean, StreamData::getStringValue).id("str2Boolean");
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
    public void getFieldFromEvent() {
        sep((c) -> {
            select(StreamData::getStringValue).id("valueString");
            select(StreamData::getDoubleValue).id("value");
        });
        Wrapper<Number> valNumber = getField("value");
        Wrapper<String> valString = getField("valueString");
        //double
        onEvent(new StreamData(23.0));
        assertThat(valNumber.event().doubleValue(), is(23.0));
        //string
        onEvent(new StreamData("test1"));
        assertThat(valString.event(), is("test1"));
    }

    @Test
    public void getFieldFromNonFluxtionEvent() {
        sep((c) -> {
            select(StreamDataNonFluxtion::getStringValue).id("valueString");
            select(StreamDataNonFluxtion::getDoubleValue).id("value");
        });
        Wrapper<Number> valNumber = getField("value");
        Wrapper<String> valString = getField("valueString");
        //double
        onGenericEvent(new StreamDataNonFluxtion("", 23.0));
        assertThat(valNumber.event().doubleValue(), is(23.0));
        //string
        onGenericEvent(new StreamDataNonFluxtion("test1", 0.0));
        assertThat(valString.event(), is("test1"));
    }

    @Data
    public static class StreamDataNonFluxtion {

        private final String stringValue;
        private final double doubleValue;
    }

    @Test
    public void mapStringFromPrimitive() {
//        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new MapFunctions()::int2String, StreamData::getIntValue).id("int2Str");
            in.map(new MapFunctions()::double2String, StreamData::getDoubleValue).id("double2Str");
            in.map(new MapFunctions()::boolean2String, StreamData::isBooleanValue).id("boolean2Str");
            in.map(new MapFunctions()::number2String, StreamData::getNumberValue).id("number2Str");
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
    public void mapRef2Ref() {
//        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(new MapFunctions()::int2Pair, StreamData::getIntValue).id("pair");
            in.map(MapStaticFunctions::int2Pair, StreamData::getIntValue).id("pairStatic");
        });
        onEvent(new StreamData(89));
        Wrapper<Pair<String, Integer>> valInstance = getField("pair");
        Wrapper<Pair<String, Integer>> valStatic = getField("pairStatic");
        assertThat(valInstance.event().getKey(), is("89"));
        assertThat(valInstance.event().getValue(), is(89));
        assertThat(valStatic.event().getKey(), is("89"));
        assertThat(valStatic.event().getValue(), is(89));

    }

    @Test
    public void mapStaticPrimitiveFromString() {
//        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(MapStaticFunctions::statStr2Int, StreamData::getStringValue).id("str2Int");
            in.map(MapStaticFunctions::statStr2Number, StreamData::getStringValue).id("str2Number");
            in.map(MapStaticFunctions::statStr2Boolean, StreamData::getStringValue).id("str2Boolean");
        });
        onEvent(new StreamData("123"));
        Wrapper<Number> valInt = getField("str2Int");
        assertThat(valInt.event().intValue(), is(123));
        Wrapper<Number> valNumber = getField("str2Number");
        assertThat(valNumber.event().intValue(), is(123));
        //boolean
        Wrapper<Boolean> val = getField("str2Boolean");
        onEvent(new StreamData("true"));
        assertThat(val.event(), is(true));
        onEvent(new StreamData("false"));
        assertThat(val.event(), is(false));
    }

    @Test
    public void mapWithLambda() {
        fixedPkg = true;
        sep((c) -> {

            Wrapper<StreamData> in = select(StreamData.class);
            in.map(s -> "mapped" + s, StreamData::getStringValue).id("str2Mapped");
            final MutableNumber result = c.addPublicNode(new MutableNumber(), "result");
            in.map(s -> 10.0 * s.getDoubleValue())
                    .map(n -> n * 20).id("mult200")
                    .push(result::setDoubleValue);
        });
//        sep(com.fluxtion.ext.declarative.builder.stream.streamingmaptest_mapwithlambda.TestSep_mapWithLambda.class);

        onEvent(new StreamData("123"));
        String str2Mapped = getWrappedField("str2Mapped");
        assertThat(str2Mapped, is("mapped123"));
        Wrapper<Number> mult200 = getField("mult200");
        Number result = getField("result");
        assertThat(mult200.event().intValue(), is(0));
        assertThat(result.intValue(), is(0));

        onEvent(new StreamData(2.0));
        assertThat(mult200.event().intValue(), is(400));
        assertThat(result.intValue(), is(400));
    }

    @Test
    public void mapStaticStringFromPrimitive() {
//        fixedPkg = true;
        sep((c) -> {
            Wrapper<StreamData> in = select(StreamData.class);
            in.map(MapStaticFunctions::int2String, StreamData::getIntValue).id("int2Str");
            in.map(MapStaticFunctions::double2String, StreamData::getDoubleValue).id("double2Str");
            in.map(MapStaticFunctions::boolean2String, StreamData::isBooleanValue).id("boolean2Str");
            in.map(MapStaticFunctions::number2String, StreamData::getNumberValue).id("number2Str");
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

}
