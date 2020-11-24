package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.sales.Shop.Sale;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions;

/**
 * generated mapper function wrapper for a numeric primitive.
 *
 * <pre>
 *  <ul>
 *   <li>template file: template/MapperPrimitiveTemplate.vsl
 *   <li>output class : {@link Number}
 *   <li>input class  : {@link Sale}
 *   <li>input class  : {@link DefaultIntWrapper}
 *   <li>map function : {@link StreamFunctions#multiply}
 *   <li>multiArg     : true
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public class Map_getAmountSold_With_multiply0 extends AbstractFilterWrapper<Number> {

  public IntFilterEventHandler filterSubject;
  private boolean filterSubjectUpdated;
  public DefaultIntWrapper source_0;
  private boolean source_0Updated;
  private double result;
  private MutableNumber value;
  private MutableNumber oldValue;

  @OnEvent
  public boolean onEvent() {
    boolean updated = true;
    if (recalculate) {
      oldValue.set(result);
      if (allSourcesUpdated()) {
        result =
            StreamFunctions.multiply(
                (double) ((Sale) filterSubject.event()).getAmountSold(),
                (double) ((Number) source_0.event()).doubleValue());
      }
      value.set(result);
      updated = allSourcesUpdated() & !notifyOnChangeOnly | (!oldValue.equals(value));
    }
    recalculate = true;
    return updated;
  }

  private boolean allSourcesUpdated() {
    boolean updated = filterSubjectUpdated | filterSubject.isValidOnStart();
    updated &= source_0Updated | source_0.isValidOnStart();
    return updated;
  }

  @OnParentUpdate("filterSubject")
  public void updated_filterSubject(IntFilterEventHandler updated) {
    filterSubjectUpdated = true;
  }

  @OnParentUpdate("source_0")
  public void updated_source_0(DefaultIntWrapper updated) {
    source_0Updated = true;
  }

  @Override
  public Number event() {
    return value;
  }

  @Override
  public Class<Number> eventClass() {
    return Number.class;
  }

  @Override
  public void reset() {
    result = 0;
    value = value == null ? new MutableNumber() : value;
    oldValue = oldValue == null ? new MutableNumber() : oldValue;
    value.set(result);
    oldValue.set(result);
    filterSubjectUpdated = false;
    recalculate = true;
    reset = false;
  }
}
