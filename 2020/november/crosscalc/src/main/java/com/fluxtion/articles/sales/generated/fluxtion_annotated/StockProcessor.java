/*
 * Copyright (C) 2018 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.articles.sales.generated.fluxtion_annotated;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.sales.Shop.Delivery;
import com.fluxtion.articles.sales.Shop.ItemCost;
import com.fluxtion.articles.sales.Shop.Price;
import com.fluxtion.articles.sales.Shop.Sale;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.numeric.DefaultNumberWrapper.DefaultIntWrapper;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Sum;
import com.fluxtion.ext.streaming.api.test.BooleanFilter;

/*
 * <pre>
 * generation time   : 2020-11-16T20:41:04.403252300
 * generator version : 2.7.9-SNAPSHOT
 * api version       : 2.7.9-SNAPSHOT
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class StockProcessor implements StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  private final IntFilterEventHandler handlerDelivery_ =
      new IntFilterEventHandler(2147483647, Delivery.class);
  private final IntFilterEventHandler handlerItemCost_ =
      new IntFilterEventHandler(2147483647, ItemCost.class);
  private final GetField_ItemCost_int0 getField_ItemCost_int0_14 = new GetField_ItemCost_int0();
  private final DefaultIntWrapper defaultIntWrapper_15 =
      new DefaultIntWrapper(getField_ItemCost_int0_14, 15);
  private final IntFilterEventHandler handlerPrice_ =
      new IntFilterEventHandler(2147483647, Price.class);
  private final GetField_Price_int0 getField_Price_int0_7 = new GetField_Price_int0();
  private final DefaultIntWrapper defaultIntWrapper_8 =
      new DefaultIntWrapper(getField_Price_int0_7, 25);
  private final IntFilterEventHandler handlerSale_ =
      new IntFilterEventHandler(2147483647, Sale.class);
  private final Map_getAmountDelivered_With_multiply0 map_getAmountDelivered_With_multiply0_16 =
      new Map_getAmountDelivered_With_multiply0();
  private final Map_getAmountSold_With_multiply0 map_getAmountSold_With_multiply0_9 =
      new Map_getAmountSold_With_multiply0();
  private final NumericPredicates numericPredicates_23 = new NumericPredicates();
  private final Sum sum_0 = new Sum();
  private final Map_getAmountSold_With_addValue0 map_getAmountSold_With_addValue0_1 =
      new Map_getAmountSold_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_2 =
      new DefaultIntWrapper(map_getAmountSold_With_addValue0_1, 0);
  private final BooleanFilter booleanFilter_10 =
      new BooleanFilter(map_getAmountSold_With_multiply0_9, defaultIntWrapper_2);
  private final Sum sum_3 = new Sum();
  private final Map_getAmountDelivered_With_addValue0 map_getAmountDelivered_With_addValue0_4 =
      new Map_getAmountDelivered_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_5 =
      new DefaultIntWrapper(map_getAmountDelivered_With_addValue0_4, 0);
  private final BooleanFilter booleanFilter_17 =
      new BooleanFilter(map_getAmountDelivered_With_multiply0_16, defaultIntWrapper_5);
  private final Map_doubleValue_With_subtract0 map_doubleValue_With_subtract0_6 =
      new Map_doubleValue_With_subtract0();
  private final Filter_Number_By_lessThan0 filter_Number_By_lessThan0_24 =
      new Filter_Number_By_lessThan0();
  private final LogMsgBuilder8 logMsgBuilder8_25 = new LogMsgBuilder8();
  private final Sum sum_11 = new Sum();
  private final Map_doubleValue_With_addValue0 map_doubleValue_With_addValue0_12 =
      new Map_doubleValue_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_13 =
      new DefaultIntWrapper(map_doubleValue_With_addValue0_12, 0);
  private final Sum sum_18 = new Sum();
  private final Map_doubleValue_With_addValue0 map_doubleValue_With_addValue0_19 =
      new Map_doubleValue_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_20 =
      new DefaultIntWrapper(map_doubleValue_With_addValue0_19, 0);
  private final Map_doubleValue_With_subtract1 map_doubleValue_With_subtract1_21 =
      new Map_doubleValue_With_subtract1();
  private final LogMsgBuilder6 logMsgBuilder6_22 = new LogMsgBuilder6();
  //Dirty flags
  private boolean isDirty_booleanFilter_10 = false;
  private boolean isDirty_booleanFilter_17 = false;
  private boolean isDirty_defaultIntWrapper_2 = false;
  private boolean isDirty_defaultIntWrapper_5 = false;
  private boolean isDirty_defaultIntWrapper_8 = false;
  private boolean isDirty_defaultIntWrapper_13 = false;
  private boolean isDirty_defaultIntWrapper_15 = false;
  private boolean isDirty_defaultIntWrapper_20 = false;
  private boolean isDirty_filter_Number_By_lessThan0_24 = false;
  private boolean isDirty_getField_ItemCost_int0_14 = false;
  private boolean isDirty_getField_Price_int0_7 = false;
  private boolean isDirty_handlerDelivery_ = false;
  private boolean isDirty_handlerItemCost_ = false;
  private boolean isDirty_handlerPrice_ = false;
  private boolean isDirty_handlerSale_ = false;
  private boolean isDirty_map_doubleValue_With_addValue0_12 = false;
  private boolean isDirty_map_doubleValue_With_addValue0_19 = false;
  private boolean isDirty_map_doubleValue_With_subtract1_21 = false;
  private boolean isDirty_map_doubleValue_With_subtract0_6 = false;
  private boolean isDirty_map_getAmountDelivered_With_addValue0_4 = false;
  private boolean isDirty_map_getAmountSold_With_addValue0_1 = false;
  //Filter constants

  public StockProcessor() {
    filter_Number_By_lessThan0_24.setAlwaysReset(false);
    filter_Number_By_lessThan0_24.setNotifyOnChangeOnly(false);
    filter_Number_By_lessThan0_24.setResetImmediate(true);
    filter_Number_By_lessThan0_24.setValidOnStart(false);
    filter_Number_By_lessThan0_24.filterSubject = map_doubleValue_With_subtract0_6;
    filter_Number_By_lessThan0_24.source_0 = map_doubleValue_With_subtract0_6;
    filter_Number_By_lessThan0_24.f = numericPredicates_23;
    getField_ItemCost_int0_14.setAlwaysReset(false);
    getField_ItemCost_int0_14.setNotifyOnChangeOnly(false);
    getField_ItemCost_int0_14.setResetImmediate(true);
    getField_ItemCost_int0_14.setValidOnStart(false);
    getField_ItemCost_int0_14.filterSubject = handlerItemCost_;
    getField_Price_int0_7.setAlwaysReset(false);
    getField_Price_int0_7.setNotifyOnChangeOnly(false);
    getField_Price_int0_7.setResetImmediate(true);
    getField_Price_int0_7.setValidOnStart(false);
    getField_Price_int0_7.filterSubject = handlerPrice_;
    logMsgBuilder6_22.setLogPrefix(false);
    logMsgBuilder6_22.source_DefaultIntWrapper_3 = defaultIntWrapper_13;
    logMsgBuilder6_22.source_DefaultIntWrapper_0 = defaultIntWrapper_2;
    logMsgBuilder6_22.source_Map_doubleValue_With_subtract1_5 = map_doubleValue_With_subtract1_21;
    logMsgBuilder6_22.source_DefaultIntWrapper_4 = defaultIntWrapper_20;
    logMsgBuilder6_22.source_Map_doubleValue_With_subtract0_2 = map_doubleValue_With_subtract0_6;
    logMsgBuilder6_22.source_DefaultIntWrapper_1 = defaultIntWrapper_5;
    logMsgBuilder6_22.logLevel = (int) 3;
    logMsgBuilder6_22.initCapacity = (int) 256;
    logMsgBuilder8_25.setLogPrefix(false);
    logMsgBuilder8_25.source_Map_doubleValue_With_subtract0_7 = map_doubleValue_With_subtract0_6;
    logMsgBuilder8_25.logNotifier = filter_Number_By_lessThan0_24;
    logMsgBuilder8_25.logLevel = (int) 3;
    logMsgBuilder8_25.initCapacity = (int) 256;
    map_doubleValue_With_addValue0_12.setAlwaysReset(false);
    map_doubleValue_With_addValue0_12.setNotifyOnChangeOnly(false);
    map_doubleValue_With_addValue0_12.setResetImmediate(true);
    map_doubleValue_With_addValue0_12.setValidOnStart(false);
    map_doubleValue_With_addValue0_12.filterSubject = booleanFilter_10;
    map_doubleValue_With_addValue0_12.f = sum_11;
    map_doubleValue_With_addValue0_19.setAlwaysReset(false);
    map_doubleValue_With_addValue0_19.setNotifyOnChangeOnly(false);
    map_doubleValue_With_addValue0_19.setResetImmediate(true);
    map_doubleValue_With_addValue0_19.setValidOnStart(false);
    map_doubleValue_With_addValue0_19.filterSubject = booleanFilter_17;
    map_doubleValue_With_addValue0_19.f = sum_18;
    map_doubleValue_With_subtract1_21.setAlwaysReset(false);
    map_doubleValue_With_subtract1_21.setNotifyOnChangeOnly(false);
    map_doubleValue_With_subtract1_21.setResetImmediate(true);
    map_doubleValue_With_subtract1_21.setValidOnStart(false);
    map_doubleValue_With_subtract1_21.filterSubject = defaultIntWrapper_13;
    map_doubleValue_With_subtract1_21.source_0 = defaultIntWrapper_20;
    map_doubleValue_With_subtract0_6.setAlwaysReset(false);
    map_doubleValue_With_subtract0_6.setNotifyOnChangeOnly(false);
    map_doubleValue_With_subtract0_6.setResetImmediate(true);
    map_doubleValue_With_subtract0_6.setValidOnStart(false);
    map_doubleValue_With_subtract0_6.filterSubject = defaultIntWrapper_5;
    map_doubleValue_With_subtract0_6.source_0 = defaultIntWrapper_2;
    map_getAmountDelivered_With_addValue0_4.setAlwaysReset(false);
    map_getAmountDelivered_With_addValue0_4.setNotifyOnChangeOnly(false);
    map_getAmountDelivered_With_addValue0_4.setResetImmediate(true);
    map_getAmountDelivered_With_addValue0_4.setValidOnStart(false);
    map_getAmountDelivered_With_addValue0_4.filterSubject = handlerDelivery_;
    map_getAmountDelivered_With_addValue0_4.f = sum_3;
    map_getAmountDelivered_With_multiply0_16.setAlwaysReset(false);
    map_getAmountDelivered_With_multiply0_16.setNotifyOnChangeOnly(false);
    map_getAmountDelivered_With_multiply0_16.setResetImmediate(true);
    map_getAmountDelivered_With_multiply0_16.setValidOnStart(false);
    map_getAmountDelivered_With_multiply0_16.filterSubject = handlerDelivery_;
    map_getAmountDelivered_With_multiply0_16.source_0 = defaultIntWrapper_15;
    map_getAmountSold_With_addValue0_1.setAlwaysReset(false);
    map_getAmountSold_With_addValue0_1.setNotifyOnChangeOnly(false);
    map_getAmountSold_With_addValue0_1.setResetImmediate(true);
    map_getAmountSold_With_addValue0_1.setValidOnStart(false);
    map_getAmountSold_With_addValue0_1.filterSubject = handlerSale_;
    map_getAmountSold_With_addValue0_1.f = sum_0;
    map_getAmountSold_With_multiply0_9.setAlwaysReset(false);
    map_getAmountSold_With_multiply0_9.setNotifyOnChangeOnly(false);
    map_getAmountSold_With_multiply0_9.setResetImmediate(true);
    map_getAmountSold_With_multiply0_9.setValidOnStart(false);
    map_getAmountSold_With_multiply0_9.filterSubject = handlerSale_;
    map_getAmountSold_With_multiply0_9.source_0 = defaultIntWrapper_8;
    numericPredicates_23.doubleLimit_0 = (double) 10.0;
    numericPredicates_23.doubleLimit_1 = (double) Double.NaN;
  }

  @Override
  public void onEvent(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.articles.sales.Shop$Delivery"):
        {
          Delivery typedEvent = (Delivery) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.sales.Shop$ItemCost"):
        {
          ItemCost typedEvent = (ItemCost) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.sales.Shop$Price"):
        {
          Price typedEvent = (Price) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.sales.Shop$Sale"):
        {
          Sale typedEvent = (Sale) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.ext.streaming.api.log.LogControlEvent"):
        {
          LogControlEvent typedEvent = (LogControlEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(Delivery typedEvent) {
    //Default, no filter methods
    isDirty_handlerDelivery_ = true;
    handlerDelivery_.onEvent(typedEvent);
    if (isDirty_handlerDelivery_) {
      map_getAmountDelivered_With_multiply0_16.updated_filterSubject(handlerDelivery_);
    }
    if (isDirty_defaultIntWrapper_15 | isDirty_handlerDelivery_) {
      map_getAmountDelivered_With_multiply0_16.onEvent();
    }
    if (isDirty_handlerDelivery_) {
      isDirty_map_getAmountDelivered_With_addValue0_4 =
          map_getAmountDelivered_With_addValue0_4.onEvent();
    }
    if (isDirty_map_getAmountDelivered_With_addValue0_4) {
      isDirty_defaultIntWrapper_5 = defaultIntWrapper_5.onEvent();
      if (isDirty_defaultIntWrapper_5) {
        map_doubleValue_With_subtract0_6.updated_filterSubject(defaultIntWrapper_5);
      }
    }
    if (isDirty_defaultIntWrapper_5) {
      isDirty_booleanFilter_17 = booleanFilter_17.updated();
    }
    if (isDirty_defaultIntWrapper_2 | isDirty_defaultIntWrapper_5) {
      isDirty_map_doubleValue_With_subtract0_6 = map_doubleValue_With_subtract0_6.onEvent();
    }
    if (isDirty_map_doubleValue_With_subtract0_6) {
      isDirty_filter_Number_By_lessThan0_24 = filter_Number_By_lessThan0_24.onEvent();
      if (isDirty_filter_Number_By_lessThan0_24) {
        logMsgBuilder8_25.postLog(filter_Number_By_lessThan0_24);
      }
    }
    if (isDirty_filter_Number_By_lessThan0_24) {
      logMsgBuilder8_25.logMessage();
    }
    if (isDirty_booleanFilter_17) {
      isDirty_map_doubleValue_With_addValue0_19 = map_doubleValue_With_addValue0_19.onEvent();
    }
    if (isDirty_map_doubleValue_With_addValue0_19) {
      isDirty_defaultIntWrapper_20 = defaultIntWrapper_20.onEvent();
      if (isDirty_defaultIntWrapper_20) {
        map_doubleValue_With_subtract1_21.updated_source_0(defaultIntWrapper_20);
      }
    }
    if (isDirty_defaultIntWrapper_13 | isDirty_defaultIntWrapper_20) {
      isDirty_map_doubleValue_With_subtract1_21 = map_doubleValue_With_subtract1_21.onEvent();
    }
    if (isDirty_defaultIntWrapper_2
        | isDirty_defaultIntWrapper_5
        | isDirty_defaultIntWrapper_13
        | isDirty_defaultIntWrapper_20
        | isDirty_map_doubleValue_With_subtract1_21
        | isDirty_map_doubleValue_With_subtract0_6) {
      logMsgBuilder6_22.logMessage();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ItemCost typedEvent) {
    //Default, no filter methods
    isDirty_handlerItemCost_ = true;
    handlerItemCost_.onEvent(typedEvent);
    if (isDirty_handlerItemCost_) {
      isDirty_getField_ItemCost_int0_14 = getField_ItemCost_int0_14.onEvent();
    }
    if (isDirty_getField_ItemCost_int0_14) {
      isDirty_defaultIntWrapper_15 = defaultIntWrapper_15.onEvent();
      if (isDirty_defaultIntWrapper_15) {
        map_getAmountDelivered_With_multiply0_16.updated_source_0(defaultIntWrapper_15);
      }
    }
    if (isDirty_defaultIntWrapper_15 | isDirty_handlerDelivery_) {
      map_getAmountDelivered_With_multiply0_16.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Price typedEvent) {
    //Default, no filter methods
    isDirty_handlerPrice_ = true;
    handlerPrice_.onEvent(typedEvent);
    if (isDirty_handlerPrice_) {
      isDirty_getField_Price_int0_7 = getField_Price_int0_7.onEvent();
    }
    if (isDirty_getField_Price_int0_7) {
      isDirty_defaultIntWrapper_8 = defaultIntWrapper_8.onEvent();
      if (isDirty_defaultIntWrapper_8) {
        map_getAmountSold_With_multiply0_9.updated_source_0(defaultIntWrapper_8);
      }
    }
    if (isDirty_defaultIntWrapper_8 | isDirty_handlerSale_) {
      map_getAmountSold_With_multiply0_9.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Sale typedEvent) {
    //Default, no filter methods
    isDirty_handlerSale_ = true;
    handlerSale_.onEvent(typedEvent);
    if (isDirty_handlerSale_) {
      map_getAmountSold_With_multiply0_9.updated_filterSubject(handlerSale_);
    }
    if (isDirty_defaultIntWrapper_8 | isDirty_handlerSale_) {
      map_getAmountSold_With_multiply0_9.onEvent();
    }
    if (isDirty_handlerSale_) {
      isDirty_map_getAmountSold_With_addValue0_1 = map_getAmountSold_With_addValue0_1.onEvent();
    }
    if (isDirty_map_getAmountSold_With_addValue0_1) {
      isDirty_defaultIntWrapper_2 = defaultIntWrapper_2.onEvent();
      if (isDirty_defaultIntWrapper_2) {
        map_doubleValue_With_subtract0_6.updated_source_0(defaultIntWrapper_2);
      }
    }
    if (isDirty_defaultIntWrapper_2) {
      isDirty_booleanFilter_10 = booleanFilter_10.updated();
    }
    if (isDirty_defaultIntWrapper_2 | isDirty_defaultIntWrapper_5) {
      isDirty_map_doubleValue_With_subtract0_6 = map_doubleValue_With_subtract0_6.onEvent();
    }
    if (isDirty_map_doubleValue_With_subtract0_6) {
      isDirty_filter_Number_By_lessThan0_24 = filter_Number_By_lessThan0_24.onEvent();
      if (isDirty_filter_Number_By_lessThan0_24) {
        logMsgBuilder8_25.postLog(filter_Number_By_lessThan0_24);
      }
    }
    if (isDirty_filter_Number_By_lessThan0_24) {
      logMsgBuilder8_25.logMessage();
    }
    if (isDirty_booleanFilter_10) {
      isDirty_map_doubleValue_With_addValue0_12 = map_doubleValue_With_addValue0_12.onEvent();
    }
    if (isDirty_map_doubleValue_With_addValue0_12) {
      isDirty_defaultIntWrapper_13 = defaultIntWrapper_13.onEvent();
      if (isDirty_defaultIntWrapper_13) {
        map_doubleValue_With_subtract1_21.updated_filterSubject(defaultIntWrapper_13);
      }
    }
    if (isDirty_defaultIntWrapper_13 | isDirty_defaultIntWrapper_20) {
      isDirty_map_doubleValue_With_subtract1_21 = map_doubleValue_With_subtract1_21.onEvent();
    }
    if (isDirty_defaultIntWrapper_2
        | isDirty_defaultIntWrapper_5
        | isDirty_defaultIntWrapper_13
        | isDirty_defaultIntWrapper_20
        | isDirty_map_doubleValue_With_subtract1_21
        | isDirty_map_doubleValue_With_subtract0_6) {
      logMsgBuilder6_22.logMessage();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        logMsgBuilder8_25.controlLogIdFilter(typedEvent);
        logMsgBuilder6_22.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        logMsgBuilder8_25.controlLogLevelFilter(typedEvent);
        logMsgBuilder6_22.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LOG_PROVIDER]
      case ("CHANGE_LOG_PROVIDER"):
        logMsgBuilder8_25.controlLogProvider(typedEvent);
        logMsgBuilder6_22.controlLogProvider(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  private void afterEvent() {
    map_doubleValue_With_addValue0_19.resetAfterEvent();
    map_doubleValue_With_addValue0_12.resetAfterEvent();
    filter_Number_By_lessThan0_24.resetAfterEvent();
    map_getAmountDelivered_With_addValue0_4.resetAfterEvent();
    map_getAmountSold_With_addValue0_1.resetAfterEvent();
    isDirty_booleanFilter_10 = false;
    isDirty_booleanFilter_17 = false;
    isDirty_defaultIntWrapper_2 = false;
    isDirty_defaultIntWrapper_5 = false;
    isDirty_defaultIntWrapper_8 = false;
    isDirty_defaultIntWrapper_13 = false;
    isDirty_defaultIntWrapper_15 = false;
    isDirty_defaultIntWrapper_20 = false;
    isDirty_filter_Number_By_lessThan0_24 = false;
    isDirty_getField_ItemCost_int0_14 = false;
    isDirty_getField_Price_int0_7 = false;
    isDirty_handlerDelivery_ = false;
    isDirty_handlerItemCost_ = false;
    isDirty_handlerPrice_ = false;
    isDirty_handlerSale_ = false;
    isDirty_map_doubleValue_With_addValue0_12 = false;
    isDirty_map_doubleValue_With_addValue0_19 = false;
    isDirty_map_doubleValue_With_subtract1_21 = false;
    isDirty_map_doubleValue_With_subtract0_6 = false;
    isDirty_map_getAmountDelivered_With_addValue0_4 = false;
    isDirty_map_getAmountSold_With_addValue0_1 = false;
  }

  @Override
  public void init() {
    getField_ItemCost_int0_14.init();
    defaultIntWrapper_15.init();
    getField_Price_int0_7.init();
    defaultIntWrapper_8.init();
    map_getAmountDelivered_With_multiply0_16.init();
    map_getAmountSold_With_multiply0_9.init();
    sum_0.reset();
    map_getAmountSold_With_addValue0_1.init();
    defaultIntWrapper_2.init();
    sum_3.reset();
    map_getAmountDelivered_With_addValue0_4.init();
    defaultIntWrapper_5.init();
    map_doubleValue_With_subtract0_6.init();
    filter_Number_By_lessThan0_24.init();
    logMsgBuilder8_25.init();
    sum_11.reset();
    map_doubleValue_With_addValue0_12.init();
    defaultIntWrapper_13.init();
    sum_18.reset();
    map_doubleValue_With_addValue0_19.init();
    defaultIntWrapper_20.init();
    map_doubleValue_With_subtract1_21.init();
    logMsgBuilder6_22.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
