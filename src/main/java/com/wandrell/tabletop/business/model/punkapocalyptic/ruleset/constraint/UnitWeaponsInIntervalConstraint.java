package com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class UnitWeaponsInIntervalConstraint implements UnitConstraint {

    private final DataModelService dataModelService;
    private final String           message;

    public UnitWeaponsInIntervalConstraint(final DataModelService service,
            final String message) {
        super();

        checkNotNull(service, "Received a null pointer as data model service");
        checkNotNull(message, "Received a null pointer as message");

        dataModelService = service;
        this.message = message;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnitWeaponsInIntervalConstraint other = (UnitWeaponsInIntervalConstraint) obj;
        return (other == this);
    }

    @Override
    public final String getErrorMessage() {
        return message;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName());
    }

    @Override
    public final Boolean isValid(final Unit unit) {
        final Boolean valid;
        final Interval interval;

        interval = getDataModelService().getUnitAllowedWeaponsInterval(
                unit.getUnitName());

        valid = ((unit.getWeapons().size() >= interval.getLowerLimit()) && (unit
                .getWeapons().size() <= interval.getUpperLimit()));

        return valid;
    }

    private final DataModelService getDataModelService() {
        return dataModelService;
    }

}
