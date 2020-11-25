package com.fluxtion.ext.declarative.builder.test;

import com.fluxtion.api.partition.LambdaReflection.SerializableConsumer;
import com.fluxtion.ext.declarative.builder.helpers.DataEvent;
import com.fluxtion.ext.declarative.builder.helpers.TestResultListener;
import com.fluxtion.ext.declarative.builder.stream.StreamInprocessTest;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.gt;
import static com.fluxtion.ext.streaming.builder.factory.EventSelect.select;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InstanceNodeTest extends StreamInprocessTest {

    @Test
    public void testSelectValidator() {
        sep(c -> {
            c.addPublicNode(new TestResultListener(
                    select(DataEvent.class).filter(DataEvent::getValue, gt(20))
            ), "results");
        }); 
        TestResultListener results = getField("results");
        DataEvent de = new DataEvent();
        sep.onEvent(de);
        assertFalse(results.receivedNotification);
        //
        de.value = 50;
        sep.onEvent(de);
        assertTrue(results.receivedNotification);
        //
        results.reset();
      de.value = 5;
        sep.onEvent(de);
        assertFalse(results.receivedNotification);
    }

    public static class NumberCompareValidators {

        public final int limit;

        public static NumberCompareValidators limit(int limit) {
            return new NumberCompareValidators(limit);
        }

        public static SerializableConsumer<Integer> gt(int limit) {
            return limit(limit)::greaterThan;
        }

        public static SerializableConsumer<Integer> lt(int limit) {
            return limit(limit)::lessThan;
        }

        public static SerializableConsumer<Integer> eq(int limit) {
            return limit(limit)::equal;
        }

        public NumberCompareValidators(int limit) {
            this.limit = limit;
        }

        public boolean greaterThan(int x) {
            return x > limit;
        }

        public boolean lessThan(int x) {
            return x < limit;
        }

        public boolean equal(int x) {
            return x == limit;
        }

    }

}
