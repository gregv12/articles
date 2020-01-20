package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.audit.Position;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Position}
 *   <li>filter function : {@link String#equals}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_getSource_By_equals0 extends AbstractFilterWrapper<Position> {

  //source operand inputs
  public ReusableEventHandler filterSubject;
  public ReusableEventHandler source_0;
  @NoEventReference public String f;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;

  @Initialise
  public void init() {
    result = false;
  }

  @OnEvent
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.equals((Object) ((Position) source_0.event()).getSource());
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
  public FilterWrapper<Position> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public Position event() {
    return (Position) filterSubject.event();
  }

  @Override
  public Class<Position> eventClass() {
    return Position.class;
  }
}
