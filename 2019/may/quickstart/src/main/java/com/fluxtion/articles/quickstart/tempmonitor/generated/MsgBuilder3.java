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
public class MsgBuilder3 extends MsgBuilder {

  //source operand inputs
  public com.fluxtion.articles.quickstart.tempmonitor.generated.Map_temp_By_max0
      source_Map_temp_By_max0_2;

  @OnEvent
  public boolean buildMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("NEW max temp ");
    msgSink.append(((java.lang.Number) source_Map_temp_By_max0_2.event()).intValue());
    msgSink.append("C");
    msgSink.append('\n');
    return true;
  }
}
