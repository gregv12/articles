package com.fluxtion.articles.quickstart.tempmonitor.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link TempEvent}
 *   <li>filter function : {@link NumericPredicates#greaterThan}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_temp_By_greaterThan0 extends AbstractFilterWrapper<TempEvent> {

  //source operand inputs
  public ReusableEventHandler filterSubject;
  public ReusableEventHandler source_0;
  @NoEventReference public NumericPredicates f;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;

  @Initialise
  public void init() {
    result = false;
  }

  @OnEvent
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.greaterThan((double) ((TempEvent) source_0.event()).temp());
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
  public FilterWrapper<TempEvent> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public TempEvent event() {
    return (TempEvent) filterSubject.event();
  }

  @Override
  public Class<TempEvent> eventClass() {
    return TempEvent.class;
  }
}
