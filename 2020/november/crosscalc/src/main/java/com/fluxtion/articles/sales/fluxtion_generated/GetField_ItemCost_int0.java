package com.fluxtion.articles.sales.fluxtion_generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.Shop.ItemCost;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated get field template.
 *
 * <pre>
 *  <ul>
 *     <li>template file    : template/MapFieldTemplate.vsl
 *     <li>output class     : {@link Number}
 *     <li>input class      : {@link ItemCost}
 *     <li>source function  : {@link ItemCost#getAmount}
 *     <li>primitive number : true
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public class GetField_ItemCost_int0 extends AbstractFilterWrapper<Number> {

  public IntFilterEventHandler filterSubject;
  private MutableNumber result;

  @OnEvent
  public boolean onEvent() {
    result.set(((ItemCost) filterSubject.event()).getAmount());
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

  @Override
  public void reset() {
    result = new MutableNumber();
  }
}
