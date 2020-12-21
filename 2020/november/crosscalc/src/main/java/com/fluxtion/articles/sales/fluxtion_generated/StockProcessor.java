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
package com.fluxtion.articles.sales.fluxtion_generated;

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
 * generation time   : 2020-12-21T10:09:08.383228900
 * generator version : 2.10.8
 * api version       : 2.10.8
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
  private final GetField_ItemCost_int0 getField_ItemCost_int0_15 = new GetField_ItemCost_int0();
  private final DefaultIntWrapper defaultIntWrapper_16 =
      new DefaultIntWrapper(getField_ItemCost_int0_15, 15);
  private final IntFilterEventHandler handlerPrice_ =
      new IntFilterEventHandler(2147483647, Price.class);
  private final GetField_Price_int0 getField_Price_int0_7 = new GetField_Price_int0();
  private final DefaultIntWrapper defaultIntWrapper_8 =
      new DefaultIntWrapper(getField_Price_int0_7, 25);
  private final IntFilterEventHandler handlerSale_ =
      new IntFilterEventHandler(2147483647, Sale.class);
  private final IntFilterEventHandler handlerString_ =
      new IntFilterEventHandler(2147483647, String.class);
  private final Map_getAmountDelivered_With_multiply0 map_getAmountDelivered_With_multiply0_17 =
      new Map_getAmountDelivered_With_multiply0();
  private final Map_getAmountSold_With_multiply0 map_getAmountSold_With_multiply0_9 =
      new Map_getAmountSold_With_multiply0();
  private final NumericPredicates numericPredicates_27 = new NumericPredicates();
  private final String string_24 = "reset";
  private final Filter_String_By_equalsIgnoreCase0 filter_String_By_equalsIgnoreCase0_25 =
      new Filter_String_By_equalsIgnoreCase0();
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
  private final BooleanFilter booleanFilter_18 =
      new BooleanFilter(map_getAmountDelivered_With_multiply0_17, defaultIntWrapper_5);
  private final Map_doubleValue_With_subtract0 map_doubleValue_With_subtract0_6 =
      new Map_doubleValue_With_subtract0();
  private final Filter_Number_By_lessThan0 filter_Number_By_lessThan0_28 =
      new Filter_Number_By_lessThan0();
  private final LogMsgBuilder12 logMsgBuilder12_29 = new LogMsgBuilder12();
  private final Sum sum_11 = new Sum();
  private final Map_doubleValue_With_addValue0 map_doubleValue_With_addValue0_12 =
      new Map_doubleValue_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_13 =
      new DefaultIntWrapper(map_doubleValue_With_addValue0_12, 0);
  private final LogMsgBuilder1 logMsgBuilder1_14 = new LogMsgBuilder1();
  private final Sum sum_19 = new Sum();
  private final Map_doubleValue_With_addValue0 map_doubleValue_With_addValue0_20 =
      new Map_doubleValue_With_addValue0();
  private final DefaultIntWrapper defaultIntWrapper_21 =
      new DefaultIntWrapper(map_doubleValue_With_addValue0_20, 0);
  private final LogMsgBuilder3 logMsgBuilder3_22 = new LogMsgBuilder3();
  private final Map_doubleValue_With_subtract1 map_doubleValue_With_subtract1_23 =
      new Map_doubleValue_With_subtract1();
  private final LogMsgBuilder10 logMsgBuilder10_26 = new LogMsgBuilder10();
  //Dirty flags
  private boolean isDirty_booleanFilter_10 = false;
  private boolean isDirty_booleanFilter_18 = false;
  private boolean isDirty_defaultIntWrapper_2 = false;
  private boolean isDirty_defaultIntWrapper_5 = false;
  private boolean isDirty_defaultIntWrapper_8 = false;
  private boolean isDirty_defaultIntWrapper_13 = false;
  private boolean isDirty_defaultIntWrapper_16 = false;
  private boolean isDirty_defaultIntWrapper_21 = false;
  private boolean isDirty_filter_Number_By_lessThan0_28 = false;
  private boolean isDirty_filter_String_By_equalsIgnoreCase0_25 = false;
  private boolean isDirty_getField_ItemCost_int0_15 = false;
  private boolean isDirty_getField_Price_int0_7 = false;
  private boolean isDirty_handlerDelivery_ = false;
  private boolean isDirty_handlerItemCost_ = false;
  private boolean isDirty_handlerPrice_ = false;
  private boolean isDirty_handlerSale_ = false;
  private boolean isDirty_handlerString_ = false;
  private boolean isDirty_map_doubleValue_With_addValue0_12 = false;
  private boolean isDirty_map_doubleValue_With_addValue0_20 = false;
  private boolean isDirty_map_doubleValue_With_subtract1_23 = false;
  private boolean isDirty_map_doubleValue_With_subtract0_6 = false;
  private boolean isDirty_map_getAmountDelivered_With_addValue0_4 = false;
  private boolean isDirty_map_getAmountSold_With_addValue0_1 = false;
  //Filter constants

  public StockProcessor() {
    filter_Number_By_lessThan0_28.setNotifyOnChangeOnly(false);
    filter_Number_By_lessThan0_28.setValidOnStart(false);
    filter_Number_By_lessThan0_28.filterSubject = map_doubleValue_With_subtract0_6;
    filter_Number_By_lessThan0_28.source_0 = map_doubleValue_With_subtract0_6;
    filter_Number_By_lessThan0_28.f = numericPredicates_27;
    filter_String_By_equalsIgnoreCase0_25.setNotifyOnChangeOnly(false);
    filter_String_By_equalsIgnoreCase0_25.setValidOnStart(false);
    filter_String_By_equalsIgnoreCase0_25.filterSubject = handlerString_;
    filter_String_By_equalsIgnoreCase0_25.source_0 = handlerString_;
    filter_String_By_equalsIgnoreCase0_25.f = string_24;
    getField_ItemCost_int0_15.setNotifyOnChangeOnly(false);
    getField_ItemCost_int0_15.setValidOnStart(false);
    getField_ItemCost_int0_15.filterSubject = handlerItemCost_;
    getField_Price_int0_7.setNotifyOnChangeOnly(false);
    getField_Price_int0_7.setValidOnStart(false);
    getField_Price_int0_7.filterSubject = handlerPrice_;
    logMsgBuilder1_14.setLogPrefix(false);
    logMsgBuilder1_14.source_DefaultIntWrapper_0 = defaultIntWrapper_13;
    logMsgBuilder1_14.logNotifier = defaultIntWrapper_13;
    logMsgBuilder1_14.logLevel = (int) 3;
    logMsgBuilder1_14.initCapacity = (int) 256;
    logMsgBuilder3_22.setLogPrefix(false);
    logMsgBuilder3_22.source_DefaultIntWrapper_2 = defaultIntWrapper_21;
    logMsgBuilder3_22.logNotifier = defaultIntWrapper_21;
    logMsgBuilder3_22.logLevel = (int) 3;
    logMsgBuilder3_22.initCapacity = (int) 256;
    logMsgBuilder10_26.setLogPrefix(false);
    logMsgBuilder10_26.source_DefaultIntWrapper_5 = defaultIntWrapper_5;
    logMsgBuilder10_26.source_DefaultIntWrapper_4 = defaultIntWrapper_2;
    logMsgBuilder10_26.source_DefaultIntWrapper_8 = defaultIntWrapper_21;
    logMsgBuilder10_26.source_Map_doubleValue_With_subtract1_9 = map_doubleValue_With_subtract1_23;
    logMsgBuilder10_26.source_Map_doubleValue_With_subtract0_6 = map_doubleValue_With_subtract0_6;
    logMsgBuilder10_26.source_DefaultIntWrapper_7 = defaultIntWrapper_13;
    logMsgBuilder10_26.logLevel = (int) 3;
    logMsgBuilder10_26.initCapacity = (int) 256;
    logMsgBuilder12_29.setLogPrefix(false);
    logMsgBuilder12_29.source_Map_doubleValue_With_subtract0_11 = map_doubleValue_With_subtract0_6;
    logMsgBuilder12_29.logNotifier = filter_Number_By_lessThan0_28;
    logMsgBuilder12_29.logLevel = (int) 3;
    logMsgBuilder12_29.initCapacity = (int) 256;
    map_doubleValue_With_addValue0_12.setNotifyOnChangeOnly(false);
    map_doubleValue_With_addValue0_12.setValidOnStart(false);
    map_doubleValue_With_addValue0_12.filterSubject = booleanFilter_10;
    map_doubleValue_With_addValue0_12.f = sum_11;
    map_doubleValue_With_addValue0_20.setNotifyOnChangeOnly(false);
    map_doubleValue_With_addValue0_20.setValidOnStart(false);
    map_doubleValue_With_addValue0_20.filterSubject = booleanFilter_18;
    map_doubleValue_With_addValue0_20.f = sum_19;
    map_doubleValue_With_subtract1_23.setNotifyOnChangeOnly(false);
    map_doubleValue_With_subtract1_23.setValidOnStart(false);
    map_doubleValue_With_subtract1_23.filterSubject = defaultIntWrapper_13;
    map_doubleValue_With_subtract1_23.source_0 = defaultIntWrapper_21;
    map_doubleValue_With_subtract0_6.setNotifyOnChangeOnly(false);
    map_doubleValue_With_subtract0_6.setValidOnStart(false);
    map_doubleValue_With_subtract0_6.filterSubject = defaultIntWrapper_5;
    map_doubleValue_With_subtract0_6.source_0 = defaultIntWrapper_2;
    map_getAmountDelivered_With_addValue0_4.setNotifyOnChangeOnly(false);
    map_getAmountDelivered_With_addValue0_4.setValidOnStart(false);
    map_getAmountDelivered_With_addValue0_4.filterSubject = handlerDelivery_;
    map_getAmountDelivered_With_addValue0_4.f = sum_3;
    map_getAmountDelivered_With_addValue0_4.resetNoPublishNotifier =
        filter_String_By_equalsIgnoreCase0_25;
    map_getAmountDelivered_With_multiply0_17.setNotifyOnChangeOnly(false);
    map_getAmountDelivered_With_multiply0_17.setValidOnStart(false);
    map_getAmountDelivered_With_multiply0_17.filterSubject = handlerDelivery_;
    map_getAmountDelivered_With_multiply0_17.source_0 = defaultIntWrapper_16;
    map_getAmountSold_With_addValue0_1.setNotifyOnChangeOnly(false);
    map_getAmountSold_With_addValue0_1.setValidOnStart(false);
    map_getAmountSold_With_addValue0_1.filterSubject = handlerSale_;
    map_getAmountSold_With_addValue0_1.f = sum_0;
    map_getAmountSold_With_addValue0_1.resetNoPublishNotifier =
        filter_String_By_equalsIgnoreCase0_25;
    map_getAmountSold_With_multiply0_9.setNotifyOnChangeOnly(false);
    map_getAmountSold_With_multiply0_9.setValidOnStart(false);
    map_getAmountSold_With_multiply0_9.filterSubject = handlerSale_;
    map_getAmountSold_With_multiply0_9.source_0 = defaultIntWrapper_8;
    defaultIntWrapper_2.setNotifyOnChangeOnly(false);
    defaultIntWrapper_2.setValidOnStart(true);
    defaultIntWrapper_2.resetNoPublishNotifier = filter_String_By_equalsIgnoreCase0_25;
    defaultIntWrapper_5.setNotifyOnChangeOnly(false);
    defaultIntWrapper_5.setValidOnStart(true);
    defaultIntWrapper_5.resetNoPublishNotifier = filter_String_By_equalsIgnoreCase0_25;
    defaultIntWrapper_8.setNotifyOnChangeOnly(false);
    defaultIntWrapper_8.setValidOnStart(true);
    defaultIntWrapper_13.setNotifyOnChangeOnly(false);
    defaultIntWrapper_13.setValidOnStart(true);
    defaultIntWrapper_16.setNotifyOnChangeOnly(false);
    defaultIntWrapper_16.setValidOnStart(true);
    defaultIntWrapper_21.setNotifyOnChangeOnly(false);
    defaultIntWrapper_21.setValidOnStart(true);
    numericPredicates_27.doubleLimit_0 = (double) 10.0;
    numericPredicates_27.doubleLimit_1 = (double) Double.NaN;
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
      case ("java.lang.String"):
        {
          String typedEvent = (String) event;
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
      map_getAmountDelivered_With_multiply0_17.updated_filterSubject(handlerDelivery_);
    }
    if (isDirty_defaultIntWrapper_16 | isDirty_handlerDelivery_) {
      map_getAmountDelivered_With_multiply0_17.onEvent();
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
      isDirty_booleanFilter_18 = booleanFilter_18.updated();
    }
    if (isDirty_defaultIntWrapper_2 | isDirty_defaultIntWrapper_5) {
      isDirty_map_doubleValue_With_subtract0_6 = map_doubleValue_With_subtract0_6.onEvent();
    }
    if (isDirty_map_doubleValue_With_subtract0_6) {
      isDirty_filter_Number_By_lessThan0_28 = filter_Number_By_lessThan0_28.onEvent();
      if (isDirty_filter_Number_By_lessThan0_28) {
        logMsgBuilder12_29.postLog(filter_Number_By_lessThan0_28);
      }
    }
    if (isDirty_filter_Number_By_lessThan0_28) {
      logMsgBuilder12_29.logMessage();
    }
    if (isDirty_booleanFilter_18) {
      isDirty_map_doubleValue_With_addValue0_20 = map_doubleValue_With_addValue0_20.onEvent();
    }
    if (isDirty_map_doubleValue_With_addValue0_20) {
      isDirty_defaultIntWrapper_21 = defaultIntWrapper_21.onEvent();
      if (isDirty_defaultIntWrapper_21) {
        logMsgBuilder3_22.postLog(defaultIntWrapper_21);
        map_doubleValue_With_subtract1_23.updated_source_0(defaultIntWrapper_21);
      }
    }
    if (isDirty_defaultIntWrapper_21) {
      logMsgBuilder3_22.logMessage();
    }
    if (isDirty_defaultIntWrapper_13 | isDirty_defaultIntWrapper_21) {
      isDirty_map_doubleValue_With_subtract1_23 = map_doubleValue_With_subtract1_23.onEvent();
    }
    if (isDirty_defaultIntWrapper_2
        | isDirty_defaultIntWrapper_5
        | isDirty_defaultIntWrapper_13
        | isDirty_defaultIntWrapper_21
        | isDirty_map_doubleValue_With_subtract1_23
        | isDirty_map_doubleValue_With_subtract0_6) {
      logMsgBuilder10_26.logMessage();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ItemCost typedEvent) {
    //Default, no filter methods
    isDirty_handlerItemCost_ = true;
    handlerItemCost_.onEvent(typedEvent);
    if (isDirty_handlerItemCost_) {
      isDirty_getField_ItemCost_int0_15 = getField_ItemCost_int0_15.onEvent();
    }
    if (isDirty_getField_ItemCost_int0_15) {
      isDirty_defaultIntWrapper_16 = defaultIntWrapper_16.onEvent();
      if (isDirty_defaultIntWrapper_16) {
        map_getAmountDelivered_With_multiply0_17.updated_source_0(defaultIntWrapper_16);
      }
    }
    if (isDirty_defaultIntWrapper_16 | isDirty_handlerDelivery_) {
      map_getAmountDelivered_With_multiply0_17.onEvent();
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
      isDirty_filter_Number_By_lessThan0_28 = filter_Number_By_lessThan0_28.onEvent();
      if (isDirty_filter_Number_By_lessThan0_28) {
        logMsgBuilder12_29.postLog(filter_Number_By_lessThan0_28);
      }
    }
    if (isDirty_filter_Number_By_lessThan0_28) {
      logMsgBuilder12_29.logMessage();
    }
    if (isDirty_booleanFilter_10) {
      isDirty_map_doubleValue_With_addValue0_12 = map_doubleValue_With_addValue0_12.onEvent();
    }
    if (isDirty_map_doubleValue_With_addValue0_12) {
      isDirty_defaultIntWrapper_13 = defaultIntWrapper_13.onEvent();
      if (isDirty_defaultIntWrapper_13) {
        logMsgBuilder1_14.postLog(defaultIntWrapper_13);
        map_doubleValue_With_subtract1_23.updated_filterSubject(defaultIntWrapper_13);
      }
    }
    if (isDirty_defaultIntWrapper_13) {
      logMsgBuilder1_14.logMessage();
    }
    if (isDirty_defaultIntWrapper_13 | isDirty_defaultIntWrapper_21) {
      isDirty_map_doubleValue_With_subtract1_23 = map_doubleValue_With_subtract1_23.onEvent();
    }
    if (isDirty_defaultIntWrapper_2
        | isDirty_defaultIntWrapper_5
        | isDirty_defaultIntWrapper_13
        | isDirty_defaultIntWrapper_21
        | isDirty_map_doubleValue_With_subtract1_23
        | isDirty_map_doubleValue_With_subtract0_6) {
      logMsgBuilder10_26.logMessage();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        logMsgBuilder12_29.controlLogIdFilter(typedEvent);
        logMsgBuilder1_14.controlLogIdFilter(typedEvent);
        logMsgBuilder3_22.controlLogIdFilter(typedEvent);
        logMsgBuilder10_26.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        logMsgBuilder12_29.controlLogLevelFilter(typedEvent);
        logMsgBuilder1_14.controlLogLevelFilter(typedEvent);
        logMsgBuilder3_22.controlLogLevelFilter(typedEvent);
        logMsgBuilder10_26.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LOG_PROVIDER]
      case ("CHANGE_LOG_PROVIDER"):
        logMsgBuilder12_29.controlLogProvider(typedEvent);
        logMsgBuilder1_14.controlLogProvider(typedEvent);
        logMsgBuilder3_22.controlLogProvider(typedEvent);
        logMsgBuilder10_26.controlLogProvider(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(String typedEvent) {
    //Default, no filter methods
    isDirty_handlerString_ = true;
    handlerString_.onEvent(typedEvent);
    if (isDirty_handlerString_) {
      isDirty_filter_String_By_equalsIgnoreCase0_25 =
          filter_String_By_equalsIgnoreCase0_25.onEvent();
      if (isDirty_filter_String_By_equalsIgnoreCase0_25) {
        map_getAmountSold_With_addValue0_1.resetNoPublishNotification(
            filter_String_By_equalsIgnoreCase0_25);
        defaultIntWrapper_2.resetNoPublishNotification(filter_String_By_equalsIgnoreCase0_25);
        map_getAmountDelivered_With_addValue0_4.resetNoPublishNotification(
            filter_String_By_equalsIgnoreCase0_25);
        defaultIntWrapper_5.resetNoPublishNotification(filter_String_By_equalsIgnoreCase0_25);
      }
    }
    //event stack unwind callbacks
    afterEvent();
  }

  private void afterEvent() {
    defaultIntWrapper_21.resetAfterEvent();
    map_doubleValue_With_addValue0_20.resetAfterEvent();
    defaultIntWrapper_13.resetAfterEvent();
    map_doubleValue_With_addValue0_12.resetAfterEvent();
    filter_Number_By_lessThan0_28.resetAfterEvent();
    defaultIntWrapper_5.resetAfterEvent();
    map_getAmountDelivered_With_addValue0_4.resetAfterEvent();
    defaultIntWrapper_2.resetAfterEvent();
    map_getAmountSold_With_addValue0_1.resetAfterEvent();
    filter_String_By_equalsIgnoreCase0_25.resetAfterEvent();
    defaultIntWrapper_8.resetAfterEvent();
    defaultIntWrapper_16.resetAfterEvent();
    isDirty_booleanFilter_10 = false;
    isDirty_booleanFilter_18 = false;
    isDirty_defaultIntWrapper_2 = false;
    isDirty_defaultIntWrapper_5 = false;
    isDirty_defaultIntWrapper_8 = false;
    isDirty_defaultIntWrapper_13 = false;
    isDirty_defaultIntWrapper_16 = false;
    isDirty_defaultIntWrapper_21 = false;
    isDirty_filter_Number_By_lessThan0_28 = false;
    isDirty_filter_String_By_equalsIgnoreCase0_25 = false;
    isDirty_getField_ItemCost_int0_15 = false;
    isDirty_getField_Price_int0_7 = false;
    isDirty_handlerDelivery_ = false;
    isDirty_handlerItemCost_ = false;
    isDirty_handlerPrice_ = false;
    isDirty_handlerSale_ = false;
    isDirty_handlerString_ = false;
    isDirty_map_doubleValue_With_addValue0_12 = false;
    isDirty_map_doubleValue_With_addValue0_20 = false;
    isDirty_map_doubleValue_With_subtract1_23 = false;
    isDirty_map_doubleValue_With_subtract0_6 = false;
    isDirty_map_getAmountDelivered_With_addValue0_4 = false;
    isDirty_map_getAmountSold_With_addValue0_1 = false;
  }

  @Override
  public void init() {
    handlerDelivery_.reset();
    handlerItemCost_.reset();
    getField_ItemCost_int0_15.reset();
    defaultIntWrapper_16.reset();
    handlerPrice_.reset();
    getField_Price_int0_7.reset();
    defaultIntWrapper_8.reset();
    handlerSale_.reset();
    handlerString_.reset();
    map_getAmountDelivered_With_multiply0_17.reset();
    map_getAmountSold_With_multiply0_9.reset();
    filter_String_By_equalsIgnoreCase0_25.reset();
    sum_0.reset();
    map_getAmountSold_With_addValue0_1.reset();
    defaultIntWrapper_2.reset();
    booleanFilter_10.reset();
    sum_3.reset();
    map_getAmountDelivered_With_addValue0_4.reset();
    defaultIntWrapper_5.reset();
    booleanFilter_18.reset();
    map_doubleValue_With_subtract0_6.reset();
    filter_Number_By_lessThan0_28.reset();
    logMsgBuilder12_29.init();
    sum_11.reset();
    map_doubleValue_With_addValue0_12.reset();
    defaultIntWrapper_13.reset();
    logMsgBuilder1_14.init();
    sum_19.reset();
    map_doubleValue_With_addValue0_20.reset();
    defaultIntWrapper_21.reset();
    logMsgBuilder3_22.init();
    map_doubleValue_With_subtract1_23.reset();
    logMsgBuilder10_26.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
