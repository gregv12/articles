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
public class MsgBuilder4 extends MsgBuilder {

  //source operand inputs
  public com.fluxtion.ext.streaming.api.ReusableEventHandler source_ReusableEventHandler_3;

  @OnEvent
  public boolean buildMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("===== Start of day ");
    msgSink.append(
        ((com.fluxtion.articles.quickstart.tempmonitor.Events.StartOfDay)
                source_ReusableEventHandler_3.event())
            .day());
    msgSink.append(" =====");
    msgSink.append('\n');
    return true;
  }
}
