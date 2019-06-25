package com.fluxtion.articles.quickstart.tempmonitor.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions;

/**
 * generated mapper function wrapper for a numeric primitive.
 *
 * <ul>
 *   <li>output class : {@link Number}
 *   <li>input class : {@link TempEvent}
 *   <li>map function : {@link StreamFunctions#asDouble}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_getTemp_With_asDouble0 extends AbstractFilterWrapper<Number> {

  public ReusableEventHandler filterSubject;
  private double result;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    oldValue.set(result);
    result = StreamFunctions.asDouble((double) ((TempEvent) filterSubject.event()).getTemp());
    value.set(result);
    return !notifyOnChangeOnly | (!oldValue.equals(value));
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
