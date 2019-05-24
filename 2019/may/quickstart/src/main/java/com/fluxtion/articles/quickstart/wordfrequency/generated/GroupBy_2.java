package com.fluxtion.articles.quickstart.wordfrequency.generated;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions.AggregateCount;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import com.fluxtion.ext.streaming.api.group.GroupByIniitialiser;
import com.fluxtion.ext.streaming.api.group.GroupByTargetMap;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.text.api.ascii.ByteBufferDelimiter;
import java.util.BitSet;
import java.util.Map;

/**
 * generated group by holder.
 *
 * <p>target class : MutableNumber
 *
 * @author Greg Higgins
 */
public final class GroupBy_2 implements GroupBy<MutableNumber> {

  @NoEventReference public Object resetNotifier;
  public ByteBufferDelimiter byteBufferDelimiter0;
  private MutableNumber target;
  private GroupByTargetMap<MutableNumber, CalculationStateGroupBy_2> calcState;

  @OnParentUpdate("byteBufferDelimiter0")
  public boolean updatebyteBufferDelimiter0(ByteBufferDelimiter event) {
    CalculationStateGroupBy_2 instance = calcState.getOrCreateInstance(event.asString());
    boolean allMatched = instance.processSource(1, event);
    target = instance.target;
    {
      int value = instance.aggregateCount1;
      value = AggregateCount.increment((int) 0, (int) value);
      target.setIntValue((int) value);
      instance.aggregateCount1 = value;
    }
    return allMatched;
  }

  @Initialise
  public void init() {
    calcState = new GroupByTargetMap<>(CalculationStateGroupBy_2.class);
  }

  @Override
  public MutableNumber value(Object key) {
    return calcState.getInstance(key).target;
  }

  @Override
  public <V extends Wrapper<MutableNumber>> Map<?, V> getMap() {
    return (Map<?, V>) calcState.getInstanceMap();
  }

  @Override
  public MutableNumber event() {
    return target;
  }

  @Override
  public Class<MutableNumber> eventClass() {
    return MutableNumber.class;
  }

  public GroupBy_2 resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @OnParentUpdate("resetNotifier")
  public void resetNotification(Object resetNotifier) {
    init();
  }
}
