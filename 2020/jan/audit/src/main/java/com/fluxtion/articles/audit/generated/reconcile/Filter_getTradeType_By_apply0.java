package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.audit.Trade;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.NodeWrapper;
import java.util.function.Function;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Trade}
 *   <li>filter function : {@link Function#apply}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_getTradeType_By_apply0 extends AbstractFilterWrapper<Trade> {

  //source operand inputs
  public NodeWrapper filterSubject;
  public NodeWrapper source_0;
  @NoEventReference public Function f;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;

  @Initialise
  public void init() {
    result = false;
  }

  @OnEvent
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.apply((Object) ((Trade) source_0.event()).getTradeType());
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
  public FilterWrapper<Trade> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public Trade event() {
    return (Trade) filterSubject.event();
  }

  @Override
  public Class<Trade> eventClass() {
    return Trade.class;
  }
}
