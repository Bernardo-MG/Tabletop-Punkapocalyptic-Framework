package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.wandrell.tabletop.data.dao.punkapocalyptic.UnitDAO;
import com.wandrell.tabletop.model.punkapocalyptic.faction.DefaultFaction;
import com.wandrell.tabletop.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.util.tag.punkapocalyptic.dao.UnitDAOAware;
import com.wandrell.util.command.ReturnCommand;

public final class GetFactionsCommand implements
        ReturnCommand<Map<String, Faction>>, UnitDAOAware {

    private UnitDAO                               daoUnit;
    private final Collection<String>              factionNames;
    private final Map<String, Collection<String>> factionUnits;

    public GetFactionsCommand(final Collection<String> factions,
            Map<String, Collection<String>> units) {
        super();

        factionNames = factions;
        factionUnits = units;
    }

    @Override
    public final Map<String, Faction> execute() {
        final Map<String, Faction> factions;

        factions = new LinkedHashMap<>();
        for (final String name : getFactionNames()) {
            factions.put(name, new DefaultFaction(name, getUnits(name)));
        }

        return factions;
    }

    @Override
    public final void setUnitDAO(final UnitDAO dao) {
        daoUnit = dao;
    }

    private final Collection<AvailabilityUnit> getUnits(final String faction) {
        final Collection<AvailabilityUnit> units;

        units = new LinkedList<>();
        for (final String unitName : getUnits().get(faction)) {
            units.add(getUnitDAO().getAvailabilityUnit(unitName));
        }

        return units;
    }

    protected final Collection<String> getFactionNames() {
        return factionNames;
    }

    protected final UnitDAO getUnitDAO() {
        return daoUnit;
    }

    protected final Map<String, Collection<String>> getUnits() {
        return factionUnits;
    }

}
