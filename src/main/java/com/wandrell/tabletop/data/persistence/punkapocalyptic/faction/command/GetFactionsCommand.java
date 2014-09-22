package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.DefaultFaction;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.util.command.ReturnCommand;

public final class GetFactionsCommand implements
        ReturnCommand<Map<String, Faction>> {

    private final Collection<String>                        factionNames;
    private final Map<String, Collection<AvailabilityUnit>> factionUnits;

    public GetFactionsCommand(final Collection<String> factions,
            Map<String, Collection<AvailabilityUnit>> units) {
        super();

        factionNames = factions;
        factionUnits = units;
    }

    @Override
    public final Map<String, Faction> execute() {
        final Map<String, Faction> factions;

        factions = new LinkedHashMap<>();
        for (final String name : getFactionNames()) {
            factions.put(name, new DefaultFaction(name, getUnits().get(name)));
        }

        return factions;
    }

    private final Collection<String> getFactionNames() {
        return factionNames;
    }

    private final Map<String, Collection<AvailabilityUnit>> getUnits() {
        return factionUnits;
    }

}
