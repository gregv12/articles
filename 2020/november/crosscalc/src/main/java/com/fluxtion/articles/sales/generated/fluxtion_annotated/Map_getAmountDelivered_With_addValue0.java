package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.Shop.Delivery;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Sum;

/**
 * generated mapper function wrapper for a numeric primitive.
 *
 * <ul>
 *   <li>template file: MapperPrimitiveTemplate.vsl
 *   <li>output class : {@link Number}
 *   <li>input class : {@link Delivery}
 *   <li>map function : {@link Sum#addValue}
 *   <li>multiArg : false
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_getAmountDelivered_With_addValue0 extends AbstractFilterWrapper<Number> {

  public IntFilterEventHandler filterSubject;
  @NoEventReference public Sum f;
  private double result;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    oldValue.set(result);
    result = f.addValue((Number) ((Delivery) filterSubject.event()).getAmountDelivered());
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

  public void reset() {
    f.reset();
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
