package com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.UnitAware;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class UnitWeaponsInIntervalConstraint implements Constraint,
        UnitAware {

    private final DataModelService dataModelService;
    private final String           message;
    private Unit                   unit;

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
    public final Boolean isValid() {
        final Boolean valid;
        final Interval interval;

        checkNotNull(unit, "Validating a null unit");

        interval = getDataModelService().getUnitAllowedWeaponsInterval(
                getUnit().getUnitName());

        valid = ((getUnit().getWeapons().size() >= interval.getLowerLimit()) && (getUnit()
                .getWeapons().size() <= interval.getUpperLimit()));

        return valid;
    }

    @Override
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

    private final DataModelService getDataModelService() {
        return dataModelService;
    }

    private final Unit getUnit() {
        return unit;
    }

}
