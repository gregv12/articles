package com.fluxtion.articles.aot_csv.generated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.aot_csv.StatsCollector;
import com.fluxtion.articles.aot_csv.generated.Map_City_With_increment0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Number}
 *   <li>push target : {@link StatsCollector#rowProcessed}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_Number_To_rowProcessed0 implements Wrapper<Push_Number_To_rowProcessed0> {

  public Map_City_With_increment0 filterSubject;

  @PushReference public StatsCollector f;

  @OnEvent
  public boolean onEvent() {
    f.rowProcessed((int) ((Number) filterSubject.event()).intValue());
    return true;
  }

  @Override
  public Push_Number_To_rowProcessed0 event() {
    return this;
  }

  @Override
  public Class<Push_Number_To_rowProcessed0> eventClass() {
    return Push_Number_To_rowProcessed0.class;
  }
}
