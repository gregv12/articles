package com.fluxtion.articles.crosscalc.generated.fluxtion_functional;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.crosscalc.MarketTick;
import com.fluxtion.articles.crosscalc.generated.fluxtion_functional.Filter_getInstrument_By_equalsIgnoreCase0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated get field template.
 *
 * <ul>
 *   <li>output class : {@link Number}
 *   <li>input class : {@link MarketTick}
 *   <li>source function : {@link MarketTick#mid}
 *   <li>primitive number : true
 * </ul>
 *
 * @author Greg Higgins
 */
public class GetField_MarketTick_double0 extends AbstractFilterWrapper<Number> {

  public Filter_getInstrument_By_equalsIgnoreCase0 filterSubject;
  private MutableNumber result;

  @OnEvent
  public boolean onEvent() {
    result.set(((MarketTick) filterSubject.event()).mid());
    return true;
  }

  @Override
  public Number event() {
    return result;
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }

  @Initialise
  public void init() {
    result = new MutableNumber();
  }
}
