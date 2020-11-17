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
public class LogMsgBuilder6 extends LogMsgBuilder {

  //source operand inputs
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_3;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_0;
  public com.fluxtion.articles.sales.generated.fluxtion_annotated.Map_doubleValue_With_subtract1
      source_Map_doubleValue_With_subtract1_5;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_4;
  public com.fluxtion.articles.sales.generated.fluxtion_annotated.Map_doubleValue_With_subtract0
      source_Map_doubleValue_With_subtract0_2;
  public com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper
      source_DefaultIntWrapper_1;

  @OnEvent
  public boolean logMessage() {
    if (!isGoodToLog()) return false;
    msgSink.append("amount sold:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_0.event()).intValue());
    msgSink.append(" amount deilvered:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_1.event()).intValue());
    msgSink.append(" stockLevel:");
    msgSink.append(((java.lang.Number) source_Map_doubleValue_With_subtract0_2.event()).intValue());
    msgSink.append(" sales turnover:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_3.event()).intValue());
    msgSink.append(" stock cost:");
    msgSink.append(((java.lang.Number) source_DefaultIntWrapper_4.event()).intValue());
    msgSink.append(" profit:");
    msgSink.append(((java.lang.Number) source_Map_doubleValue_With_subtract1_5.event()).intValue());
    msgSink.append("");
    log();
    return true;
  }
}
