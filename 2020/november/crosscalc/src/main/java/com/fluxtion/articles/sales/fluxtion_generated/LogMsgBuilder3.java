package com.fluxtion.articles.sales.fluxtion_generated;

import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.log.LogMsgBuilder;
/**
 * Generated notificationToLogger.
 *
 * @author Greg Higgins
 */
public class LogMsgBuilder3 extends LogMsgBuilder {

  //source operand inputs
  @NoEventReference
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_2;

  public Object logNotifier;
  private boolean notificationToLog;

  @OnParentUpdate(value = "logNotifier")
  public void postLog(Object logNotifier) {
    notificationToLog = true;
  }

  @OnEvent
  public boolean logMessage() {
    if (notificationToLog & isGoodToLog()) {
      msgSink.append("stock cost: ");
      msgSink.append(((java.lang.Number) source_DefaultIntWrapper_2.event()).intValue());
      msgSink.append("");
      notificationToLog = false;
      log();
      return true;
    }
    notificationToLog = false;
    return false;
  }
}
