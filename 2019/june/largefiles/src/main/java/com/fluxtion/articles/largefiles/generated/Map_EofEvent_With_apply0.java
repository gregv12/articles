package com.fluxtion.articles.largefiles.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.text.api.event.EofEvent;
import java.util.function.Function;

/**
 * Generated mapper function wrapper for a reference type.
 *
 * <ul>
 *   <li>output class : {@link Object}
 *   <li>input class : {@link EofEvent}
 *   <li>map function : {@link Function#apply}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_EofEvent_With_apply0 extends AbstractFilterWrapper<Object> {

  public ReusableEventHandler filterSubject;
  private boolean filterSubjectUpdated;
  @NoEventReference public Function f;
  private Object result;

  @OnEvent
  public boolean onEvent() {
    Object oldValue = result;
    if (filterSubjectUpdated) {
      result = f.apply((Object) ((EofEvent) filterSubject.event()));
    }
    return !notifyOnChangeOnly || (!result.equals(oldValue));
  }

  private boolean allSourcesUpdated() {
    boolean updated = filterSubjectUpdated;
    return updated;
  }

  @OnParentUpdate("filterSubject")
  public void updated_filterSubject(ReusableEventHandler updated) {
    filterSubjectUpdated = true;
  }

  @Override
  public Object event() {
    return result;
  }

  @Override
  public Class<Object> eventClass() {
    return Object.class;
  }

  @Initialise
  public void init() {
    result = null;
    filterSubjectUpdated = false;
  }
}
