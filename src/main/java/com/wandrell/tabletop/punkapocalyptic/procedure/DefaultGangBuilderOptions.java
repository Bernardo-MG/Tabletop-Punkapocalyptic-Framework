package com.wandrell.tabletop.punkapocalyptic.procedure;

import java.util.Collection;

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
        return getFactionUnitAvailabilityRepository().getUnitsForFaction(
                getFactionName());
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
