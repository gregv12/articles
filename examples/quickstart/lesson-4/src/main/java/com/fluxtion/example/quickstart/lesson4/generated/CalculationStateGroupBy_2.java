package com.fluxtion.example.quickstart.lesson4.generated;

import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.GroupByIniitialiser;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Sum;
import com.fluxtion.ext.streaming.api.util.Tuple;
import java.util.BitSet;
import java.util.Collection;

/**
 * Generated group by calculation state holder.
 *
 * <pre>
 *  <ul>
 *      <li>template file   : template/GroupByCalculationState.vsl
 *      <li>input class     : target class  : Tuple
 *  </ul>
 * </pre>
 *
 * @author Greg Higgins
 */
public final class CalculationStateGroupBy_2 implements Wrapper<Tuple> {

  private static final int SOURCE_COUNT = 1;
  final BitSet updateMap = new BitSet(SOURCE_COUNT);
  private final MutableNumber tempNumber = new MutableNumber();
  private int combineCount;
  public Tuple target;
  public Sum sum1Function = new Sum();
  public double sum1;

  public CalculationStateGroupBy_2() {
    target = new Tuple();
  }

  public boolean allMatched() {
    return SOURCE_COUNT == updateMap.cardinality();
  }

  /**
   * @param index
   * @param initialiser
   * @param source
   * @return The first time this is a complete record is processed
   */
  public boolean processSource(int index, GroupByIniitialiser initialiser, Object source) {
    boolean prevMatched = allMatched();
    if (!updateMap.get(index)) {
      initialiser.apply(source, target);
    }
    updateMap.set(index);
    return allMatched() ^ prevMatched;
  }

  /**
   * @param index
   * @param source
   * @return The first time this is a complete record is processed
   */
  public boolean processSource(int index, Object source) {
    boolean prevMatched = allMatched();
    updateMap.set(index);
    return allMatched() ^ prevMatched;
  }

  @Override
  public Tuple event() {
    return target;
  }

  @Override
  public Class<Tuple> eventClass() {
    return Tuple.class;
  }

  @Override
  public String toString() {
    return event().toString();
  }

  public void combine(CalculationStateGroupBy_2 other) {
    //list the combining operations
    combineCount++;
    sum1 = sum1Function.combine(other.sum1Function, tempNumber).doubleValue();
  }

  public void deduct(CalculationStateGroupBy_2 other) {
    combineCount--;
    sum1 = sum1Function.deduct(other.sum1Function, tempNumber).doubleValue();
  }

  public void aggregateNonStateful(Collection<CalculationStateGroupBy_2> states) {
    //TODO
  }

  public int getCombineCount() {
    return combineCount;
  }

  public boolean isExpired() {
    return combineCount < 1;
  }
}
