package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.Shop.Price;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated get field template.
 *
 * <ul>
 *   <li>output class : {@link Number}
 *   <li>input class : {@link Price}
 *   <li>source function : {@link Price#getCustomerPrice}
 *   <li>primitive number : true
 * </ul>
 *
 * @author Greg Higgins
 */
public class GetField_Price_int0 extends AbstractFilterWrapper<Number> {

  public IntFilterEventHandler filterSubject;
  private MutableNumber result;

  @OnEvent
  public boolean onEvent() {
    result.set(((Price) filterSubject.event()).getCustomerPrice());
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
