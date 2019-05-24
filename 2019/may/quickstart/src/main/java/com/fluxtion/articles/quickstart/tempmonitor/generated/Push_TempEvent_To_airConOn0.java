package com.fluxtion.articles.quickstart.tempmonitor.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.quickstart.tempmonitor.EnvironmentalController;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.articles.quickstart.tempmonitor.generated.Filter_temp_By_greaterThan0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link TempEvent}
 *   <li>push target : {@link EnvironmentalController#airConOn}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_TempEvent_To_airConOn0 implements Wrapper<Push_TempEvent_To_airConOn0> {

  public Filter_temp_By_greaterThan0 filterSubject;
  @PushReference public EnvironmentalController f;

  @OnEvent
  public boolean onEvent() {
    f.airConOn((TempEvent) ((TempEvent) filterSubject.event()));
    return true;
  }

  @Override
  public Push_TempEvent_To_airConOn0 event() {
    return this;
  }

  @Override
  public Class<Push_TempEvent_To_airConOn0> eventClass() {
    return (Class<Push_TempEvent_To_airConOn0>) getClass();
  }
}
