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
public class MsgBuilder3 extends MsgBuilder {

  //source operand inputs
  public com.fluxtion.articles.audit.generated.reconcile.Filter_getTradeType_By_apply0
      source_Filter_getTradeType_By_apply0_2;

  @OnEvent
  public boolean buildMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("<- pushing client trade to colo [");
    msgSink.append(
        ((com.fluxtion.articles.audit.Trade) source_Filter_getTradeType_By_apply0_2.event())
            .toString());
    msgSink.append("]");
    msgSink.append('\n');
    return true;
  }
}
