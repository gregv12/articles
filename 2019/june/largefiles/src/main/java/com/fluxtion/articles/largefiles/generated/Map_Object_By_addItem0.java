package com.fluxtion.articles.largefiles.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.largefiles.generated.Map_Voter_By_apply0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.ListCollector;
import java.util.List;

/**
 * Generated mapper function wrapper for a reference type.
 *
 * <ul>
 *   <li>output class : {@link List}
 *   <li>input class : {@link Object}
 *   <li>map function : {@link ListCollector#addItem}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Map_Object_By_addItem0 extends AbstractFilterWrapper<List> {

  public Map_Voter_By_apply0 filterSubject;
  private boolean filterSubjectUpdated;
  @NoEventReference public ListCollector f;
  private List result;
  @NoEventReference public Object resetNotifier;
  private boolean parentReset = false;

  @OnEvent
  public boolean onEvent() {
    List oldValue = result;
    if (filterSubjectUpdated) {
      result = f.addItem((Object) ((Object) filterSubject.event()));
    }
    return !notifyOnChangeOnly || (!result.equals(oldValue));
  }

  private boolean allSourcesUpdated() {
    boolean updated = filterSubjectUpdated;
    return updated;
  }

  @OnParentUpdate("filterSubject")
  public void updated_filterSubject(Map_Voter_By_apply0 updated) {
    filterSubjectUpdated = true;
  }

  @OnParentUpdate("resetNotifier")
  public void resetNotification(Object resetNotifier) {
    parentReset = true;
    if (isResetImmediate()) {
      f.reset();
      parentReset = false;
    }
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (parentReset | alwaysReset) {
      f.reset();
    }
    parentReset = false;
  }

  @Override
  public FilterWrapper<List> resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @Override
  public List event() {
    return result;
  }

  @Override
  public Class<List> eventClass() {
    return List.class;
  }

  @Initialise
  public void init() {
    result = null;
    filterSubjectUpdated = false;
  }
}
