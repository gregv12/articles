package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.log.MsgBuilder;
/**
 * Generated notificationToLogger.
 *
 * @author Greg Higgins
 */
public class MsgBuilder1 extends MsgBuilder {

  //source operand inputs
  public com.fluxtion.ext.streaming.api.ReusableEventHandler source_ReusableEventHandler_0;

  @OnEvent
  public boolean buildMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("-> received [");
    msgSink.append(
        ((com.fluxtion.articles.audit.Position) source_ReusableEventHandler_0.event()).toString());
    msgSink.append("]");
    msgSink.append('\n');
    return true;
  }
}
