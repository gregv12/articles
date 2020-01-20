package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.audit.Trade;
import com.fluxtion.articles.audit.generated.reconcile.Map_getTerms_With_value0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Number}
 *   <li>push target : {@link Trade#setTerms}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_Number_To_setTerms0 implements Wrapper<Push_Number_To_setTerms0> {

  public Map_getTerms_With_value0 filterSubject;
  @PushReference public Trade f;

  @OnEvent
  public boolean onEvent() {
    f.setTerms((double) ((Number) filterSubject.event()).doubleValue());
    return true;
  }

  @Override
  public Push_Number_To_setTerms0 event() {
    return this;
  }

  @Override
  public Class<Push_Number_To_setTerms0> eventClass() {
    return (Class<Push_Number_To_setTerms0>) getClass();
  }
}
