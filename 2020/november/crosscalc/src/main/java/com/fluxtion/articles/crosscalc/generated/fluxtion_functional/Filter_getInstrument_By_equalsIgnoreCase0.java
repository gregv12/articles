package com.fluxtion.articles.crosscalc.generated.fluxtion_functional;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.crosscalc.MarketTick;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link MarketTick}
 *   <li>filter function : {@link String#equalsIgnoreCase}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_getInstrument_By_equalsIgnoreCase0 extends AbstractFilterWrapper<MarketTick> {

  //source operand inputs
  public IntFilterEventHandler filterSubject;
  public IntFilterEventHandler source_0;
  @NoEventReference public String f;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;

  @Initialise
  public void init() {
    result = false;
  }

  @OnEvent
  @SuppressWarnings("unchecked")
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.equalsIgnoreCase((String) ((MarketTick) source_0.event()).getInstrument());
    //this is probably right - to be tested
    //return (!notifyOnChangeOnly | !oldValue) & result;
    return (!notifyOnChangeOnly & result) | ((!oldValue) & result);
  }

  @OnParentUpdate("resetNotifier")
  public void resetNotification(Object resetNotifier) {
    parentReset = true;
    if (isResetImmediate()) {
      result = false;
      parentReset = false;
    }
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (parentReset) {
      result = false;
    }
    parentReset = false;
  }

  @Override
  public FilterWrapper<MarketTick> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public MarketTick event() {
    return (MarketTick) filterSubject.event();
  }

  @Override
  public Class<MarketTick> eventClass() {
    return MarketTick.class;
  }
}
