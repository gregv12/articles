package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * generated filter function wrapper.
 *
 * <pre>
 *  <ul>
 *      <li>template file   : template/FilterTemplate.vsl
 *      <li>input class     : {@link String}
 *      <li>filter function : {@link String#equalsIgnoreCase}
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public class Filter_String_By_equalsIgnoreCase0 extends AbstractFilterWrapper<String> {

  //source operand inputs
  public IntFilterEventHandler filterSubject;
  public IntFilterEventHandler source_0;
  @NoEventReference public String f;

  @OnEvent
  @SuppressWarnings("unchecked")
  public boolean onEvent() {
    boolean oldValue = result;
    result = (boolean) f.equalsIgnoreCase((String) ((String) filterSubject.event()));
    return (!notifyOnChangeOnly | !oldValue) & result;
    //return (!notifyOnChangeOnly & result) | ((!oldValue) & result);
  }

  @AfterEvent
  public void resetAfterEvent() {
    if (reset) {
      result = false;
    }
    reset = false;
  }

  @Override
  public String event() {
    return (String) filterSubject.event();
  }

  @Override
  public Class<String> eventClass() {
    return String.class;
  }

  @Override
  public void reset() {
    //add override logic
    result = false;
  }
}
