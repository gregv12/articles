package com.fluxtion.examples.tradingmonitor.generated.symbol;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.examples.tradingmonitor.AssetTradePos;
import com.fluxtion.examples.tradingmonitor.generated.symbol.Map_AssetPrice_By_increment0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Number}
 *   <li>push target : {@link AssetTradePos#setPricesProcessed}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_Number_To_setPricesProcessed0
    implements Wrapper<Push_Number_To_setPricesProcessed0> {

  public Map_AssetPrice_By_increment0 filterSubject;
  @PushReference public AssetTradePos f;

  @OnEvent
  public boolean onEvent() {
    f.setPricesProcessed((int) ((Number) filterSubject.event()).intValue());
    return true;
  }

  @Override
  public Push_Number_To_setPricesProcessed0 event() {
    return this;
  }

  @Override
  public Class<Push_Number_To_setPricesProcessed0> eventClass() {
    return (Class<Push_Number_To_setPricesProcessed0>) getClass();
  }
}
