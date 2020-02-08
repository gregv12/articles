package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.audit.Position;
import com.fluxtion.articles.audit.Trade;
import com.fluxtion.articles.audit.generated.reconcile.Filter_getSource_By_equals0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Position}
 *   <li>push target : {@link Trade#setTermsCcy}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_getTermsCcy_To_setTermsCcy0 implements Wrapper<Push_getTermsCcy_To_setTermsCcy0> {

  public Filter_getSource_By_equals0 filterSubject;
  @PushReference public Trade f;

  @OnEvent
  public boolean onEvent() {
    f.setTermsCcy((String) ((Position) filterSubject.event()).getTermsCcy());
    return true;
  }

  @Override
  public Push_getTermsCcy_To_setTermsCcy0 event() {
    return this;
  }

  @Override
  public Class<Push_getTermsCcy_To_setTermsCcy0> eventClass() {
    return Push_getTermsCcy_To_setTermsCcy0.class;
  }
}
