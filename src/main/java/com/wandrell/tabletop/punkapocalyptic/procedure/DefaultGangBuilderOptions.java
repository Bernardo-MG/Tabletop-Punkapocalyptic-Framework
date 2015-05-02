package com.wandrell.tabletop.punkapocalyptic.procedure;

import java.util.Collection;
import java.util.LinkedList;

import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.UnitTemplate;
import com.wandrell.tabletop.punkapocalyptic.repository.FactionUnitAvailabilityRepository;

public final class DefaultGangBuilderOptions implements GangBuilderOptions {

    private String                                  factionName;
    private final FactionUnitAvailabilityRepository unitAvaRepository;

    public DefaultGangBuilderOptions(
            final FactionUnitAvailabilityRepository unitAvaRepository) {
        super();

        this.unitAvaRepository = unitAvaRepository;
    }

    @Override
    public final Collection<UnitTemplate> getUnitOptions() {
        final Collection<UnitTemplate> result;
        final Collection<FactionUnitAvailability> avas;

        avas = getFactionUnitAvailabilityRepository()
                .getAvailabilitiesForFaction(getFactionName());

        result = new LinkedList<>();
        for (final FactionUnitAvailability ava : avas) {
            result.add(ava.getUnit());
        }

        return result;
    }

    public final void setFactionName(final String name) {
        factionName = name;
    }

    private final String getFactionName() {
        return factionName;
    }

    private final FactionUnitAvailabilityRepository
            getFactionUnitAvailabilityRepository() {
        return unitAvaRepository;
    }

}
