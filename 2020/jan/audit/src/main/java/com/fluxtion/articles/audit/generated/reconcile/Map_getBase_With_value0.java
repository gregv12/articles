package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.audit.Position;
import com.fluxtion.articles.audit.generated.reconcile.Filter_getSource_By_equals0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Delta;

/**
 * generated mapper function wrapper for a numeric primitive.
 *
 * <ul>
 *   <li>output class : {@link Number}
 *   <li>input class : {@link Position}
 *   <li>map function : {@link Delta#value}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_getBase_With_value0 extends AbstractFilterWrapper<Number> {

  public Filter_getSource_By_equals0 filterSubject;
  @NoEventReference public Delta f;
  private double result;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    oldValue.set(result);
    result = f.value((double) ((Position) filterSubject.event()).getBase());
    value.set(result);
    return !notifyOnChangeOnly | (!oldValue.equals(value));
  }

  @OnParentUpdate("resetNotifier")
  public void resetNotification(Object resetNotifier) {
    parentReset = true;
    if (isResetImmediate()) {
      result = 0;
      f.reset();
      parentReset = false;
    }
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (parentReset | alwaysReset) {
      result = 0;
      f.reset();
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
    return value;
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }

  @Initialise
  public void init() {
    result = 0;
    value = new MutableNumber();
    oldValue = new MutableNumber();
  }
}
