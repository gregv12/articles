package com.fluxtion.articles.quickstart.tempmonitor.generated;

import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.log.MsgBuilder;
/**
 * Generated notificationToLogger.
 *
 * @author Greg Higgins
 */
public class MsgBuilder10 extends MsgBuilder {

  //source operand inputs
  public com.fluxtion.ext.streaming.api.test.BooleanFilter source_BooleanFilter_9;

  @OnEvent
  public boolean buildMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("NEW day avg temp ");
    msgSink.append(((java.lang.Number) source_BooleanFilter_9.event()).doubleValue());
    msgSink.append("C");
    msgSink.append('\n');
    return true;
  }
}
