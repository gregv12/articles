package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.log.LogMsgBuilder;
/**
 * Generated notificationToLogger.
 *
 * @author Greg Higgins
 */
public class LogMsgBuilder12 extends LogMsgBuilder {

  //source operand inputs
  @NoEventReference
  public com.fluxtion.articles.sales.generated.fluxtion_annotated.Map_doubleValue_With_subtract0
      source_Map_doubleValue_With_subtract0_11;

  public Object logNotifier;
  private boolean notificationToLog;

  @OnParentUpdate(value = "logNotifier")
  public void postLog(Object logNotifier) {
    notificationToLog = true;
  }

  @OnEvent
  public boolean logMessage() {
    if (notificationToLog & isGoodToLog()) {
      msgSink.append("Warning low stock level order more items, current level:");
      msgSink.append(
          ((java.lang.Number) source_Map_doubleValue_With_subtract0_11.event()).intValue());
      msgSink.append("");
      notificationToLog = false;
      log();
      return true;
    }
    notificationToLog = false;
    return false;
  }
}
