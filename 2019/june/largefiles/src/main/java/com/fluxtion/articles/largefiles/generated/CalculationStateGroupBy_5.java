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
 * generated group by calculation state holder. This class holds the state of a group by
 * calculation.
 *
 * <p>target class : MutableNumber
 *
 * @author Greg Higgins
 */
public final class CalculationStateGroupBy_5 implements Wrapper<MutableNumber> {

  private static final int SOURCE_COUNT = 1;
  private final BitSet updateMap = new BitSet(SOURCE_COUNT);

  public MutableNumber target;
  public int aggregateCount4;

  public CalculationStateGroupBy_5() {
    target = new MutableNumber();
  }

  public boolean allMatched() {
    return SOURCE_COUNT == updateMap.cardinality();
  }

  public boolean processSource(int index, GroupByIniitialiser initialiser, Object source) {
    if (!updateMap.get(index)) {
      initialiser.apply(source, target);
    }
    updateMap.set(index);
    return allMatched();
  }

  public boolean processSource(int index, Object source) {
    updateMap.set(index);
    return allMatched();
  }

  @Override
  public MutableNumber event() {
    return target;
  }

  @Override
  public Class<MutableNumber> eventClass() {
    return MutableNumber.class;
  }

  @Override
  public String toString() {
    return event().toString();
  }
}
