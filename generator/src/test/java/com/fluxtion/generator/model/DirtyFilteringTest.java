/* 
 * Copyright (C) 2018 V12 Technology Ltd.
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
package com.fluxtion.generator.model;

import com.fluxtion.generator.model.TopologicallySortedDependecyGraph;
import com.fluxtion.generator.model.SimpleEventProcessorModel;
import com.fluxtion.test.event.AnnotatedEventHandlerDirtyNotifier;
import com.fluxtion.test.event.DirtyNotifierNode;
import com.fluxtion.test.event.EventHandlerCb;
import com.fluxtion.test.event.InitCB;
import com.fluxtion.test.event.RootCB;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Greg Higgins
 */
public class DirtyFilteringTest {

    public DirtyFilteringTest() {
    }

    @Test
    public void testGetNodeGuardConditions() throws Exception {
        //set up modes
        EventHandlerCb e1 = new EventHandlerCb("e1", 1);
        EventHandlerCb e2 = new EventHandlerCb("e2", 2);
        InitCB i1 = new InitCB("i1");
        InitCB i2 = new InitCB("i2");
        InitCB i3 = new InitCB("i3");
        DirtyNotifierNode dirty_1 = new DirtyNotifierNode("dirty_1");
        DirtyNotifierNode dirty_2 = new DirtyNotifierNode("dirty_2");
        DirtyNotifierNode dirty_3 = new DirtyNotifierNode("dirty_3");
        RootCB eRoot = new RootCB("eRoot");

        dirty_1.parents = new Object[]{e1};
        dirty_2.parents = new Object[]{e2};
        dirty_3.parents = new Object[]{i1, i3};
        i1.parents = new Object[]{e1};
        i2.parents = new Object[]{dirty_1, dirty_2};
        i3.parents = new Object[]{e2};
        eRoot.parents = new Object[]{dirty_3};

        List<Object> nodeList = Arrays.asList(eRoot, e1, e2, dirty_1, dirty_2, dirty_3, i1, i2, i3);
        //generate model
        TopologicallySortedDependecyGraph graph = new TopologicallySortedDependecyGraph(nodeList);
        SimpleEventProcessorModel sep = new SimpleEventProcessorModel(graph);
        sep.generateMetaModel(true);
        //
        //System.out.println(sep.getDirtyFieldMap());

        sep.getFieldForInstance(dirty_1);

        assertThat(sep.getDirtyFieldMap().keySet(), containsInAnyOrder(
                sep.getFieldForInstance(dirty_1),
                sep.getFieldForInstance(dirty_2),
                sep.getFieldForInstance(dirty_3)
        ));

        assertThat(sep.getNodeGuardConditions(i2), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_1),
                sep.getDirtyFlagForNode(dirty_2)
        ));

        assertThat(sep.getNodeGuardConditions(eRoot), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_3)
        ));

        assertTrue(sep.getNodeGuardConditions(e1).isEmpty());
        assertTrue(sep.getNodeGuardConditions(e2).isEmpty());
        assertTrue(sep.getNodeGuardConditions(i1).isEmpty());
        assertTrue(sep.getNodeGuardConditions(i3).isEmpty());
        assertTrue(sep.getNodeGuardConditions(dirty_3).isEmpty());

        //using new method
        assertThat(sep.getNodeGuardConditions_OLD(i2), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_1),
                sep.getDirtyFlagForNode(dirty_2)
        ));

        assertThat(sep.getNodeGuardConditions_OLD(eRoot), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_3)
        ));

        assertTrue(sep.getNodeGuardConditions_OLD(e1).isEmpty());
        assertTrue(sep.getNodeGuardConditions_OLD(e2).isEmpty());
        assertTrue(sep.getNodeGuardConditions_OLD(i1).isEmpty());
        assertTrue(sep.getNodeGuardConditions_OLD(i3).isEmpty());
        assertTrue(sep.getNodeGuardConditions_OLD(dirty_3).isEmpty());
    }

    /**
     * A test for proving guard conditions are transferred through nodes when
     * the interstitial node does not support dirty filtering
     */
    @Test
    public void testGuardConditionDiscontinuous() throws Exception {
        //set up modes
        EventHandlerCb e1 = new EventHandlerCb("e1", 1);
        EventHandlerCb e2 = new EventHandlerCb("e2", 2);
        InitCB i1 = new InitCB("i1");
        InitCB i2 = new InitCB("i2");
        InitCB i3 = new InitCB("i3");
        InitCB i4 = new InitCB("i4");
        InitCB i5 = new InitCB("i5");
        InitCB i6 = new InitCB("i6");

        DirtyNotifierNode dirty_1 = new DirtyNotifierNode("dirty_1");
        DirtyNotifierNode dirty_2 = new DirtyNotifierNode("dirty_2");

        dirty_1.parents = new Object[]{e1};
        dirty_2.parents = new Object[]{e2};

        i1.parents = new Object[]{e2};
        i2.parents = new Object[]{dirty_1};
        i3.parents = new Object[]{dirty_1, dirty_2};
        i4.parents = new Object[]{i1, i3};
        i5.parents = new Object[]{i2};
        i6.parents = new Object[]{i3};

        List<Object> nodeList = Arrays.asList(e1, e2,
                dirty_1, dirty_2,
                i1, i2, i3, i4, i5, i6);
        //generate model
        TopologicallySortedDependecyGraph graph = new TopologicallySortedDependecyGraph(nodeList);
        SimpleEventProcessorModel sep = new SimpleEventProcessorModel(graph);
        sep.generateMetaModel(true);

        assertThat(sep.getNodeGuardConditions(i5), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_1)
        ));

        assertThat(sep.getNodeGuardConditions(i6), containsInAnyOrder(
                sep.getDirtyFlagForNode(dirty_1),
                sep.getDirtyFlagForNode(dirty_2)
        ));
    }

    @Test
    public void testEventHandlerWithDirtySupport() throws Exception {
        AnnotatedEventHandlerDirtyNotifier eh = new AnnotatedEventHandlerDirtyNotifier();
        DirtyNotifierNode dirty_1 = new DirtyNotifierNode("dirty_1", eh);
        
        //generate model
        TopologicallySortedDependecyGraph graph = new TopologicallySortedDependecyGraph(Arrays.asList(eh, dirty_1));
        SimpleEventProcessorModel sep = new SimpleEventProcessorModel(graph);
        sep.generateMetaModel(true);
        
        assertThat(sep.getNodeGuardConditions(dirty_1), containsInAnyOrder(
                sep.getDirtyFlagForNode(eh)
        ));
    }
}