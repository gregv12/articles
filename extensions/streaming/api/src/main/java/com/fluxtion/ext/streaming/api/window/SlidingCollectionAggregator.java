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
package com.fluxtion.ext.streaming.api.window;

import com.fluxtion.api.SepContext;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.ext.streaming.api.ArrayListWrappedCollection;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.WrappedCollection;
import com.fluxtion.ext.streaming.api.WrappedList;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.StreamOperator;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 * @param <T>
 */
public class SlidingCollectionAggregator<T extends WrappedCollection> {

    private final Object notifier;
    @NoEventReference
    private final WrappedCollection<T, ?, ?> source;
    private final int size;
    @PushReference
    private ArrayListWrappedCollection<T> targetCollection;
    private ArrayDeque<Stateful> deque;
    @NoEventReference
    private TimeReset timeReset;

    public SlidingCollectionAggregator(Object notifier, WrappedCollection<T, ?, ?> source, int size) {
        this.notifier = notifier;
        this.source = source;
        this.size = size;
        targetCollection =  SepContext.service().addOrReuse(new ArrayListWrappedCollection<>());
    }

    public ArrayListWrappedCollection<T> getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(ArrayListWrappedCollection<T> targetCollection) {
        this.targetCollection = targetCollection;
    }

    public TimeReset getTimeReset() {
        return timeReset;
    }

    public void setTimeReset(TimeReset timeReset) {
        this.timeReset = timeReset;
    }
    
    public  SlidingCollectionAggregator<T> id(String id) {
        return StreamOperator.service().nodeId(this, id);
    }
    
    @OnEvent
    public void aggregate() {
        //remove
        int expiredBuckete = timeReset==null?1:timeReset.getWindowsExpired();
        if(expiredBuckete==0){
            return;
        }
        Stateful popped = deque.poll();
        targetCollection.deduct(popped);
        for (int i = 1; i < expiredBuckete; i++) {
            Stateful popped2 = deque.poll();
            targetCollection.deduct(popped2);
            popped2.reset();
            deque.add(popped2);
        }
        popped.reset();
        popped.combine(source);
        deque.add(popped);
        //add
        targetCollection.combine(source);
    }

    public WrappedList<T> comparator(Comparator comparator) {
        return SepContext.service().addOrReuse(targetCollection).comparator(comparator);
    }

    public List<T> collection() {
        return targetCollection.collection();
    }

    @Initialise
    public void init() {
        try {
            deque = new ArrayDeque<>(size);
            targetCollection.reset();
            for (int i = 0; i < size; i++) {
                final Stateful function = new ArrayListWrappedCollection<>();
                function.reset();
                deque.add(function);
            }
        } catch (Exception ex) {
            throw new RuntimeException("missing default constructor for:" + source.getClass());
        }
    }

}