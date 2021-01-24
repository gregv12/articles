package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.audit.HouseTradePublisher;
import com.fluxtion.articles.audit.Trade;
import com.fluxtion.articles.audit.generated.reconcile.Filter_getTradeType_By_apply0;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;

/**
 * Generated push function wrapper.
 *
 * <ul>
 *   <li>input class : {@link Trade}
 *   <li>push target : {@link HouseTradePublisher#publishHouseTrade}
 * </ul>
 *
 * @author Greg Higgins
 */
public class Push_Trade_To_publishHouseTrade0 implements Wrapper<Push_Trade_To_publishHouseTrade0> {

  public Filter_getTradeType_By_apply0 filterSubject;
  @PushReference public HouseTradePublisher f;

  @OnEvent
  public boolean onEvent() {
    f.publishHouseTrade((Trade) ((Trade) filterSubject.event()));
    return true;
  }

  @Override
  public Push_Trade_To_publishHouseTrade0 event() {
    return this;
  }

  @Override
  public Class<Push_Trade_To_publishHouseTrade0> eventClass() {
    return Push_Trade_To_publishHouseTrade0.class;
  }
}
