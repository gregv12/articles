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
package com.fluxtion.generator.audit;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.audit.EventLogControlEvent.LogLevel;
import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.api.audit.StructuredLogRecord;
import com.fluxtion.generator.util.BaseSepInprocessTest;
import com.fluxtion.generator.util.YamlLogRecordListener;
import com.fluxtion.test.event.CharEvent;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class TraceLogTest extends BaseSepInprocessTest {

    @Test
    public void testNoTrace() {
        fixedPkg = true;
        YamlLogRecordListener yamlRecord = new YamlLogRecordListener();
        sep(c -> {
            ParentNode parent = c.addNode(new ParentNode(), "parent");
            ChildNode child = c.addNode(new ChildNode(parent), "child");
            GrandChildNode grandChild = c.addNode(new GrandChildNode(child), "grandChild");
            c.addNode(new GreatGrandChildNode(grandChild), "greatGrandChild");
            c.addAuditor(new EventLogManager(), "sampleLogger");
        });
        onEvent(new EventLogControlEvent(yamlRecord));
//        JULLogRecordListener julRecord = new JULLogRecordListener();
//        onEvent(new EventLogControlEvent(julRecord));
        onEvent(new CharEvent('a'));

        List<StructuredLogRecord> eventList = yamlRecord.getEventList();
        assertThat(1, is(eventList.size()));
        assertThat("CharEvent", is(eventList.get(0).getEventType()));
        final List<StructuredLogRecord.AuditRecord> auditLogs = eventList.get(0).getAuditLogs();
        assertThat(2, is(auditLogs.size()));
        //check first is parent then child
        assertThat("parent", is(auditLogs.get(0).getNodeId()));
        assertThat("child", is(auditLogs.get(1).getNodeId()));
        //check parent has right char
        assertThat("a", is(auditLogs.get(0).getPropertyMap().get("char")));
    }

    @Test
    public void testWithTrace() {
        fixedPkg = true;
        YamlLogRecordListener yamlRecord = new YamlLogRecordListener();
        sep(c -> {
            ParentNode parent = c.addNode(new ParentNode(), "parent");
            ChildNode child = c.addNode(new ChildNode(parent), "child");
            GrandChildNode grandChild = c.addNode(new GrandChildNode(child), "grandChild");
            c.addNode(new GreatGrandChildNode(grandChild), "greatGrandChild");
            c.addAuditor(new EventLogManager().tracingOn(LogLevel.INFO).printEventToString(false), "sampleLogger");
        });
        onEvent(new EventLogControlEvent(yamlRecord));
        onEvent(new CharEvent('b'));

        List<StructuredLogRecord> eventList = yamlRecord.getEventList();
        assertThat(2, is(eventList.size()));
        assertThat("EventLogControlEvent", is(eventList.get(0).getEventType()));
        assertThat("CharEvent", is(eventList.get(1).getEventType()));
        final List<StructuredLogRecord.AuditRecord> auditLogs = eventList.get(1).getAuditLogs();
        assertThat(auditLogs.size(), is(4));
        //check first is parent then child
        assertThat("parent", is(auditLogs.get(0).getNodeId()));
        assertThat("child", is(auditLogs.get(1).getNodeId()));
        assertThat("grandChild", is(auditLogs.get(2).getNodeId()));
        assertThat("greatGrandChild", is(auditLogs.get(3).getNodeId()));
        //check parent has right char
        assertThat("b", is(auditLogs.get(0).getPropertyMap().get("char")));

    }

    @Test
    public void testWithTraceFinestLevel() {
        fixedPkg = true;
        YamlLogRecordListener yamlRecord = new YamlLogRecordListener();
        sep(c -> {
            ParentNode parent = c.addNode(new ParentNode(), "parent");
            ChildNode child = c.addNode(new ChildNode(parent), "child");
            GrandChildNode grandChild = c.addNode(new GrandChildNode(child), "grandChild");
            c.addNode(new GreatGrandChildNode(grandChild), "greatGrandChild");
            c.addAuditor(new EventLogManager().tracingOn(LogLevel.TRACE).printEventToString(false), "sampleLogger");
        });
        onEvent(new EventLogControlEvent(yamlRecord));
        onEvent(new CharEvent('c'));//2 audit
        onEvent(new EventLogControlEvent(LogLevel.TRACE));//0 audit
        onEvent(new CharEvent('d'));//4 audit

        List<StructuredLogRecord> eventList = yamlRecord.getEventList();
        assertThat(4, is(eventList.size()));
        assertThat("CharEvent", is(eventList.get(1).getEventType()));
        List<StructuredLogRecord.AuditRecord> auditLogs = eventList.get(1).getAuditLogs();
        assertThat(2, is(auditLogs.size()));
        //check first is parent then child
        assertThat("parent", is(auditLogs.get(0).getNodeId()));
        assertThat("child", is(auditLogs.get(1).getNodeId()));
        //check parent has right char
        assertThat("c", is(auditLogs.get(0).getPropertyMap().get("char")));
        //check control events
        assertThat("EventLogControlEvent", is(eventList.get(0).getEventType()));
        assertThat("EventLogControlEvent", is(eventList.get(2).getEventType()));
        //now should be on trace
        auditLogs = eventList.get(3).getAuditLogs();
        assertThat(auditLogs.size(), is(4));
        //check first is parent then child
        assertThat("parent", is(auditLogs.get(0).getNodeId()));
        assertThat("child", is(auditLogs.get(1).getNodeId()));
        assertThat("grandChild", is(auditLogs.get(2).getNodeId()));
        assertThat("greatGrandChild", is(auditLogs.get(3).getNodeId()));
        //check parent has right char
        assertThat("d", is(auditLogs.get(0).getPropertyMap().get("char")));
    }

    public static class ParentNode extends EventLogNode {

        @Inject
        public MyNode myNode;

        @EventHandler
        public void charEvent(CharEvent event) {
            log.info("char", event.getChar());
        }

    }

    public static class ChildNode extends EventLogNode {

        private final ParentNode parent;

        public ChildNode(ParentNode parent) {
            this.parent = parent;
        }

        @OnEvent
        public void onEvent() {
            log.info("child", true);
        }
    }

    public static class GrandChildNode extends EventLogNode {

        private final ChildNode parent;

        public GrandChildNode(ChildNode parent) {
            this.parent = parent;
        }

        @OnEvent
        public void onEvent() {
        }
    }

    public static class GreatGrandChildNode {

        private final GrandChildNode parent;

        public GreatGrandChildNode(GrandChildNode parent) {
            this.parent = parent;
        }

        @OnEvent
        public void onEvent() {
        }
    }

    public static class MyNode {

        public boolean registerCalled = false;

    }

}
