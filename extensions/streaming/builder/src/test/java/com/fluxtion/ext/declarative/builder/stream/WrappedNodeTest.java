package com.fluxtion.ext.declarative.builder.stream;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.builder.stream.StreamOperatorService;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class WrappedNodeTest extends StreamInprocessTest {

    @Test
    public void mapPrimitiveFromString() {
        sep((c) -> {
            StreamDataHandler handler = c.addNode(new StreamDataHandler());
            Wrapper<StreamDataHandler> in = StreamOperatorService.stream(handler);
            in.map(new MapFunctions()::String2Number, StreamDataHandler::stringValue).id("str2Number");
        });
        
        onEvent(new StreamData("23"));
        //int
        Wrapper<Number> valNumber = getField("str2Number");
        assertThat(valNumber.event().intValue(), is(23));
    }

    public static class StreamDataHandler  {

        private StreamData event;

        @EventHandler
        public boolean handleStreamData(StreamData event) {
            this.event = event;
            return true;
        }

        public String stringValue() {
            return event.getStringValue();
        }

        public void stringValue(String stringValue) {
            event.setStringValue(stringValue);
        }

//        public int getIntValue() {
//            return event.getIntValue();
//        }
//
//        public void setIntValue(int intValue) {
//            event.setIntValue(intValue);
//        }
//
//        public double getDoubleValue() {
//            return event.getDoubleValue();
//        }
//
//        public void setDoubleValue(double doubleValue) {
//            event.setDoubleValue(doubleValue);
//        }
//
//        public boolean isBooleanValue() {
//            return event.isBooleanValue();
//        }
//
//        public void setBooleanValue(boolean booleanValue) {
//            event.setBooleanValue(booleanValue);
//        }
//
//        public Number getNumberValue() {
//            return event.getNumberValue();
//        }

    }

}
