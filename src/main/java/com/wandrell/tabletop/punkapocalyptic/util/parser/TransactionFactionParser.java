package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Map;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.model.faction.DefaultFaction;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;

public final class TransactionFactionParser implements
        Parser<Map<String, Object>, Faction> {

    public TransactionFactionParser() {
        super();
    }

    @Override
    public final Faction parse(final Map<String, Object> input) {
        // TODO: Use a service
        return new DefaultFaction(input.get("name").toString());
    }

}
