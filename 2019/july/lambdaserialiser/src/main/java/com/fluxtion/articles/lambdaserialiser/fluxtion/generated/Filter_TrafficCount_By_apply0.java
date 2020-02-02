package com.fluxtion.articles.lambdaserialiser.fluxtion.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.lambdaserialiser.fluxtion.ProcessBuilder.TrafficCount;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import java.util.function.Function;

/**
 * generated filter function wrapper.
 *
 * <ul>
 *   <li>input class : {@link TrafficCount}
 *   <li>filter function : {@link Function#apply}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Filter_TrafficCount_By_apply0 extends AbstractFilterWrapper<TrafficCount> {

  //source operand inputs
  public IntFilterEventHandler filterSubject;
  public IntFilterEventHandler source_0;
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
    result = (boolean) f.apply((Object) ((TrafficCount) filterSubject.event()));
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
  public FilterWrapper<TrafficCount> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public TrafficCount event() {
    return (TrafficCount) filterSubject.event();
  }

  @Override
  public Class<TrafficCount> eventClass() {
    return TrafficCount.class;
  }
}
