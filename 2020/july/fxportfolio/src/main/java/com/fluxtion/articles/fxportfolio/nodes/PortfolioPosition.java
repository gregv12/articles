/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.fxportfolio.nodes;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.PUBLISH_POSITIONS;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.PUBLISH_POSITIONS_STOP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author greg higgins <greg.higgins@v12technology.com>
 */
public class PortfolioPosition extends EventLogNode {

    public List<ManagedAsset> assetList = new ArrayList();
    private Map<Ccy, ManagedAsset> assetMap = new HashMap();
    private boolean publish = false;

    public void addAsset(ManagedAsset asset) {
        assetList.add(asset);
    }

    @EventHandler(filterString = PUBLISH_POSITIONS)
    public void publishPositions(Signal signal) {
        publish = true;
        publish();
    }

    @EventHandler(filterString = PUBLISH_POSITIONS_STOP)
    public void publishPositionsStop(Signal signal) {
        publish = false;
    }

    @OnEvent
    public void publish() {
        if (publish) {
            assetList.stream().forEach(a -> log.info(a.currency.name(), a.position()));
        }
    }

    @Initialise
    public void init() {
    }

}
