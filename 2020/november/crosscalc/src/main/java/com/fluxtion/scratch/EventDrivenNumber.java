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
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
//@Data
public class EventDrivenNumber extends MutableNumber{
    
    private transient String filter;
    
    @EventHandler(filterVariable = "filter")
    public boolean updateNumber(NumericSignal signal){
        boolean updated = super.equals(signal.getNumber());
        return updated;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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
        final EventDrivenNumber other = (EventDrivenNumber) obj;
        if (!Objects.equals(this.filter, other.filter)) {
            return false;
        }
        return true;
    }
    
}
