package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.DefaultFaction;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.util.command.ReturnCommand;

public final class GetFactionsCommand implements
        ReturnCommand<Map<String, Faction>> {

    private final Collection<String> factionNames;

    public GetFactionsCommand(final Collection<String> factions) {
        super();

        factionNames = factions;
    }

    @Override
    public final Map<String, Faction> execute() {
        final Map<String, Faction> factions;

        factions = new LinkedHashMap<>();
        for (final String name : getFactionNames()) {
            factions.put(name, new DefaultFaction(name));
        }

        return factions;
    }

    private final Collection<String> getFactionNames() {
        return factionNames;
    }

}
