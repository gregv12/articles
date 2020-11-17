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
package com.fluxtion.scratch;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import java.util.Objects;

/**
 *
 * @author V12 Technology Ltd.
 */
//@Data
public class EventMutableNumber implements Wrapper<Number> {

    private transient String filter;
    private final MutableNumber number = new MutableNumber();
    private boolean validOnStart;
    
    public static class EventMutableInt extends EventMutableNumber{
    
        private int intValue;
    }
    
    public static class EventMutableDouble extends EventMutableNumber{
    
        private double doubleValue;
    }

    public static class EventMutableLong extends EventMutableNumber{
    
        private long longValue;
    }
    
    @EventHandler(filterVariable = "filter")
    public boolean updateNumber(NumericSignal signal) {
        boolean updated = true;//super.equals(signal.getNumber());
        return updated;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getIntValue() {
        return number.intValue();
    }

    public long getLongValue() {
        return number.longValue();
    }

    public double getDoubleValue() {
        return number.doubleValue();
    }

    public void setIntValue(int intValue) {
        number.setIntValue(intValue);
    }

    public void setLongValue(long longValue) {
        number.setLongValue(longValue);
    }

    public void setDoubleValue(double doubleValue) {
        number.setDoubleValue(doubleValue);
    }

    public void set(int value) {
        number.set(value);
    }

    public void set(long value) {
        number.set(value);
    }

    public void set(double value) {
        number.set(value);
    }
    
    @OnEvent
    public boolean onEvent() {
        return true;
    }

    @Override
    public Number event() {
        return number;
    }

    @Override
    public Class<Number> eventClass() {
        return Number.class;
    }

    @Override
    public boolean isValidOnStart() {
        return validOnStart;
    }

//    @Override
    public void setValidOnStart(boolean validOnStart) {
        this.validOnStart = validOnStart;
    }

    @Override
    public Wrapper<Number> validOnStart(boolean validOnStart) {
        this.validOnStart = validOnStart;
        return this;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.filter);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventMutableNumber other = (EventMutableNumber) obj;
        if (!Objects.equals(this.filter, other.filter)) {
            return false;
        }
        return true;
    }

}
