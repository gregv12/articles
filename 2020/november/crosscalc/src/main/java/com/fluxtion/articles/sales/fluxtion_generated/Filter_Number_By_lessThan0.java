package com.fluxtion.articles.sales.fluxtion_generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.fluxtion_generated.Map_doubleValue_With_subtract0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;

/**
 * generated filter function wrapper.
 *
 * <pre>
 *  <ul>
 *      <li>template file   : template/FilterTemplate.vsl
 *      <li>input class     : {@link Number}
 *      <li>filter function : {@link NumericPredicates#lessThan}
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public class Filter_Number_By_lessThan0 extends AbstractFilterWrapper<Number> {

  //source operand inputs
  public Map_doubleValue_With_subtract0 filterSubject;
  public Map_doubleValue_With_subtract0 source_0;
  @NoEventReference public NumericPredicates f;

  @OnEvent
  @SuppressWarnings("unchecked")
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.lessThan((double) ((Number) filterSubject.event()).doubleValue());
    return (!notifyOnChangeOnly | !oldValue) & result;
    //return (!notifyOnChangeOnly & result) | ((!oldValue) & result);
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (reset) {
      result = false;
    }
    reset = false;
  }

  @Override
  public Number event() {
    return (Number) filterSubject.event();
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }

  @Override
  public void reset() {
    //add override logic
    result = false;
  }
}
