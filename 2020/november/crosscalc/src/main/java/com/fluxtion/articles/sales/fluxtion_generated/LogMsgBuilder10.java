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
public class LogMsgBuilder10 extends LogMsgBuilder {

  //source operand inputs
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_5;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_4;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_8;
  public com.fluxtion.articles.sales.fluxtion_generated.Map_doubleValue_With_subtract1
      source_Map_doubleValue_With_subtract1_9;
  public com.fluxtion.articles.sales.fluxtion_generated.Map_doubleValue_With_subtract0
      source_Map_doubleValue_With_subtract0_6;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_7;

  @OnEvent
  public boolean logMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("amount sold:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_4.event()).intValue());
    msgSink.append(" amount deilvered:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_5.event()).intValue());
    msgSink.append(" stockLevel:");
    msgSink.append(((java.lang.Number) source_Map_doubleValue_With_subtract0_6.event()).intValue());
    msgSink.append(" sales turnover:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_7.event()).intValue());
    msgSink.append(" stock cost:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_8.event()).intValue());
    msgSink.append(" profit:");
    msgSink.append(((java.lang.Number) source_Map_doubleValue_With_subtract1_9.event()).intValue());
    msgSink.append("");
    log();
    return true;
  }
}
