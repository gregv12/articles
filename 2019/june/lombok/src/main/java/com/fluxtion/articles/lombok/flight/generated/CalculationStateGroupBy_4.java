package com.fluxtion.articles.lombok.flight.generated;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.articles.lombok.flight.FlightAnalyser.CarrierDelay;
import com.fluxtion.articles.lombok.flight.FlightAnalyser.FlightDetails;
import com.fluxtion.articles.lombok.flight.generated.Filter_getDelay_By_positiveInt0;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions.AggregateAverage;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions.AggregateCount;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions.AggregateSum;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import com.fluxtion.ext.streaming.api.group.GroupByIniitialiser;
import com.fluxtion.ext.streaming.api.group.GroupByTargetMap;
import java.util.BitSet;
import java.util.Map;

/**
 * generated group by calculation state holder. This class holds the state of a group by
 * calculation.
 *
 * <p>target class : CarrierDelay
 *
 * @author Greg Higgins
 */
public final class CalculationStateGroupBy_4 implements Wrapper<CarrierDelay> {

  private static final int SOURCE_COUNT = 1;
  private final BitSet updateMap = new BitSet(SOURCE_COUNT);

  public CarrierDelay target;
  public AggregateAverage aggregateAverage1Function = new AggregateAverage();
  public double aggregateAverage1;
  public double aggregateSum3;
  public int aggregateCount2;

  public CalculationStateGroupBy_4() {
    target = new CarrierDelay();
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
  public CarrierDelay event() {
    return target;
  }

  @Override
  public Class<CarrierDelay> eventClass() {
    return CarrierDelay.class;
  }

  @Override
  public String toString() {
    return event().toString();
  }
}
