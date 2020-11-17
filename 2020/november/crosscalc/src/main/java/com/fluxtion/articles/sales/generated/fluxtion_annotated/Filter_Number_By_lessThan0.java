package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.generated.fluxtion_annotated.Map_doubleValue_With_subtract0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Number}
 *   <li>filter function : {@link NumericPredicates#lessThan}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_Number_By_lessThan0 extends AbstractFilterWrapper<Number> {

  //source operand inputs
  public Map_doubleValue_With_subtract0 filterSubject;
  public Map_doubleValue_With_subtract0 source_0;
  @NoEventReference public NumericPredicates f;
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
    result = (boolean) f.lessThan((double) ((Number) filterSubject.event()).doubleValue());
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
  public FilterWrapper<Number> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public Number event() {
    return (Number) filterSubject.event();
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }
}
