package com.fluxtion.articles.sales.fluxtion_generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.Shop.Sale;
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
 * <pre>
 *  <ul>
 *   <li>template file: template/MapperPrimitiveTemplate.vsl
 *   <li>output class : {@link Number}
 *   <li>input class  : {@link Sale}
 *   <li>map function : {@link Sum#addValue}
 *   <li>multiArg     : false
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public class Map_getAmountSold_With_addValue0 extends AbstractFilterWrapper<Number> {

  public IntFilterEventHandler filterSubject;
  @NoEventReference public Sum f;
  private double result;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    boolean updated = true;
    if (recalculate) {
      oldValue.set(result);
      result = f.addValue((Number) ((Sale) filterSubject.event()).getAmountSold());
      value.set(result);
      updated = !notifyOnChangeOnly | (!oldValue.equals(value));
    }
    recalculate = true;
    return updated;
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (reset) {
      reset();
    }
    reset = false;
  }

  @Override
  public Number event() {
    return value;
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }

  @Override
  public void reset() {
    result = 0;
    value = value == null ? new MutableNumber() : value;
    oldValue = oldValue == null ? new MutableNumber() : oldValue;
    value.set(result);
    oldValue.set(result);
    f.reset();
    recalculate = true;
    reset = false;
  }
}
