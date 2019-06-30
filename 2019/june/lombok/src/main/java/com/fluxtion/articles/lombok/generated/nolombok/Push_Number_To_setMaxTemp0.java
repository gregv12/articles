package com.fluxtion.articles.lombok.generated.nolombok;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.lombok.Main.MyTempProcessor;
import com.fluxtion.articles.lombok.generated.nolombok.Map_Number_With_max0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Number}
 *   <li>push target : {@link MyTempProcessor#setMaxTemp}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_Number_To_setMaxTemp0 implements Wrapper<Push_Number_To_setMaxTemp0> {

  public Map_Number_With_max0 filterSubject;
  @PushReference public MyTempProcessor f;

  @OnEvent
  public boolean onEvent() {
    f.setMaxTemp((double) ((Number) filterSubject.event()).doubleValue());
    return true;
  }

  @Override
  public Push_Number_To_setMaxTemp0 event() {
    return this;
  }

  @Override
  public Class<Push_Number_To_setMaxTemp0> eventClass() {
    return (Class<Push_Number_To_setMaxTemp0>) getClass();
  }
}
