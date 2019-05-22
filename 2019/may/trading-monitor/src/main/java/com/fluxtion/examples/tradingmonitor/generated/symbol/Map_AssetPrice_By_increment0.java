package com.fluxtion.examples.tradingmonitor.generated.symbol;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.examples.tradingmonitor.AssetPrice;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;

/**
 * generated mapper function wrapper for a numeric primitive.
 *
 * <ul>
 *   <li>output class : {@link Number}
 *   <li>input class : {@link AssetPrice}
 *   <li>map function : {@link Count#increment}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_AssetPrice_By_increment0 extends AbstractFilterWrapper<Number> {

  public ReusableEventHandler filterSubject;
  private boolean filterSubjectUpdated;
  @NoEventReference public Count f;
  private int result;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    oldValue.set(result);
    if (filterSubjectUpdated) {
      result = f.increment((Object) ((AssetPrice) filterSubject.event()));
    }
    value.set(result);
    return !notifyOnChangeOnly | (!oldValue.equals(value));
  }

  private boolean allSourcesUpdated() {
    boolean updated = filterSubjectUpdated;
    return updated;
  }

  @OnParentUpdate("filterSubject")
  public void updated_filterSubject(ReusableEventHandler updated) {
    filterSubjectUpdated = true;
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
    filterSubjectUpdated = false;
  }
}
