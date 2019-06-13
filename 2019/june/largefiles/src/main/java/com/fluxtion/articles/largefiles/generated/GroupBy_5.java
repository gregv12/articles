package com.fluxtion.articles.largefiles.generated;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.largefiles.Voter;
import com.fluxtion.articles.largefiles.generated.VoterCsvDecoder0;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions.AggregateCount;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import com.fluxtion.ext.streaming.api.group.GroupByIniitialiser;
import com.fluxtion.ext.streaming.api.group.GroupByTargetMap;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import java.util.BitSet;
import java.util.Map;

/**
 * generated group by holder.
 *
 * <p>target class : MutableNumber
 *
 * @author Greg Higgins
 */
public final class GroupBy_5 implements GroupBy<MutableNumber> {

  @NoEventReference public Object resetNotifier;
  public VoterCsvDecoder0 voterCsvDecoder00;
  private MutableNumber target;
  private GroupByTargetMap<MutableNumber, CalculationStateGroupBy_5> calcState;

  @OnParentUpdate("voterCsvDecoder00")
  public boolean updatevoterCsvDecoder00(VoterCsvDecoder0 eventWrapped) {
    Voter event = (Voter) eventWrapped.event();
    CalculationStateGroupBy_5 instance = calcState.getOrCreateInstance(event.getDateString());
    boolean allMatched = instance.processSource(1, event);
    target = instance.target;
    {
      int value = instance.aggregateCount4;
      value = AggregateCount.increment((int) event.hashCode(), (int) value);
      target.set((double) value);
      instance.aggregateCount4 = value;
    }
    return allMatched;
  }

  @Initialise
  public void init() {
    calcState = new GroupByTargetMap<>(CalculationStateGroupBy_5.class);
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

  public GroupBy_5 resetNotifier(Object resetNotifier) {
    this.resetNotifier = resetNotifier;
    return this;
  }

  @OnParentUpdate("resetNotifier")
  public void resetNotification(Object resetNotifier) {
    init();
  }
}
