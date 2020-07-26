/*
 * Copyright (C) 2020 V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.articles.fxportfolio.nodes.ManagedAsset;
import com.fluxtion.articles.fxportfolio.nodes.PairHedge;
import com.fluxtion.articles.fxportfolio.nodes.PortfolioPosition;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.builder.node.SEPConfig;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FluxtionBuilder {

    private SEPConfig cfg;
    private Set<ManagedAsset> managedAssets = new HashSet<>();
    private Set<PairHedge> hedgeSet = new HashSet<>();

    public void buildPortfolioCalc(SEPConfig cfg) {
        this.cfg = cfg;
        
        addManagedAsset("CHF", "USDCHF", "EURCHF");
        addManagedAsset("USD", "USDGBP");
        addManagedAsset("EUR", "EURGBP", "EURUSD");
        addManagedAsset("GBP");
        buildPositionDependencies();
        cfg.addAuditor(new EventLogManager(), "eventLogger");
//        cfg.addAuditor(new EventLogManager().tracingOn(EventLogControlEvent.LogLevel.INFO), "eventLogger");
    }

    private void addManagedAsset(String ccy, String... hedges) {
        ManagedAsset managedAsset = cfg.addNode(new ManagedAsset(Ccy.valueOf(ccy)), "position" + ccy);
        managedAssets.add(managedAsset);
        for (String hedge : hedges) {
            PairHedge hedgeNode = cfg.addNode(new PairHedge(hedge), "hedge" + hedge);
            addHedger(managedAsset, hedgeNode);
            hedgeSet.add(hedgeNode);
        }
    }

    public void addHedger(ManagedAsset managedAsset, PairHedge hedger) {
        if (managedAsset.hedger1 == null) {
            managedAsset.hedger1 = hedger;
        } else if (managedAsset.hedger2 == null) {
            managedAsset.hedger2 = hedger;
        } else if (managedAsset.hedger3 == null) {
            managedAsset.hedger3 = hedger;
        } else if (managedAsset.hedger4 == null) {
            managedAsset.hedger4 = hedger;
        } else {
            throw new IndexOutOfBoundsException("only four hedge routes supported");
        }
    }

    private void buildPositionDependencies() {
        PortfolioPosition portfolioPosition = cfg.addNode(new PortfolioPosition(), "portfolioPosition");
        managedAssets.forEach(m -> {
            portfolioPosition.addAsset(m);
            hedgeSet.forEach(h -> {
                if ((m.hedger1 != h && m.hedger2 != h && m.hedger3 != h && m.hedger4 != h)
                        && h.ccyPair().containsCcy(m.currency)) {
                    m.openPositions.add(h);
                }
            });
        });
    }

}
