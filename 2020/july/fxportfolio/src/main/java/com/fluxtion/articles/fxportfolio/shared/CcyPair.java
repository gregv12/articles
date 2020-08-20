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
package com.fluxtion.articles.fxportfolio.shared;

import java.io.IOException;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
@Data
public class CcyPair {

    public Ccy base;
    public Ccy terms;
    public String name;

    public CcyPair() {
    }
    
    public CcyPair(String pair) {
        this(Ccy.valueOf(pair.substring(0, 3)), Ccy.valueOf(pair.substring(3)));
    }

    public CcyPair( Ccy terms, Ccy base) {
        this.terms = terms;
        this.base = base;
        this.name = terms.toString() + base.toString();
    }
  
    public static CcyPair ccyPairFromCharSeq(CharSequence ccyPair){
        return new CcyPair(ccyPair.toString());
    }
  
    public static void ccyPairToCharSequence(CcyPair ccyPair, Appendable msg) throws IOException{
        msg.append(ccyPair.name);
    }
    
    public boolean containsCcy(Ccy ccy) {
        return base == ccy || terms == ccy;
    }

    @Override
    public String toString() {
        return "CcyPair{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.base);
        hash = 47 * hash + Objects.hashCode(this.terms);
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
        final CcyPair other = (CcyPair) obj;
        if (this.base != other.base) {
            return false;
        }
        if (this.terms != other.terms) {
            return false;
        }
        return true;
    }
    
    
}
