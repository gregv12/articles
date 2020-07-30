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

import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.articles.fxportfolio.nodes.ManagedAsset;
import com.fluxtion.articles.fxportfolio.nodes.PairHedge;
import com.fluxtion.articles.fxportfolio.nodes.PortfolioPosition;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.builder.node.SEPConfig;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FluxtionBuilder {

    private SEPConfig cfg;
    private final Map<String, List<String>> data;
    private final Set<ManagedAsset> managedAssets = new HashSet<>();
    private final Set<PairHedge> hedgeSet = new HashSet<>();

    public FluxtionBuilder(String configAsString) {
        this(new StringReader(configAsString));
    }

    public FluxtionBuilder(Reader configReader) {
        Yaml yaml = new Yaml();
        data = (Map<String, List<String>>) yaml.load(configReader);
    }

    public void buildPortfolioCalcFromFile(SEPConfig cfg) {
        this.cfg = cfg;
        data.forEach(this::addManagedAsset);
        buildPositionDependencies();
        cfg.addAuditor(new EventLogManager(), "eventLogger");
//        cfg.addAuditor(new EventLogManager().tracingOn(EventLogControlEvent.LogLevel.INFO), "eventLogger");
    }

    private void addManagedAsset(String ccy, List<String> hedges) {
        ManagedAsset managedAsset = cfg.addNode(new ManagedAsset(Ccy.valueOf(ccy)), "position" + ccy);
        managedAssets.add(managedAsset);
        for (String hedge : hedges) {
            PairHedge hedgeNode = cfg.addNode(new PairHedge(hedge), "hedge" + hedge);
            managedAsset.hedgeList.add(hedgeNode);
            hedgeSet.add(hedgeNode);
        }
    }

    private void buildPositionDependencies() {
        PortfolioPosition portfolioPosition = cfg.addNode(new PortfolioPosition(), "portfolioPosition");
        managedAssets.forEach(m -> {
            portfolioPosition.addAsset(m);
            hedgeSet.forEach(h -> {
                if (!m.hedgeList.contains(h) && h.ccyPair().containsCcy(m.currency)) {
                    m.openPositions.add(h);
                }
            });
        });
    }

}
