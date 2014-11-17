package com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;

public final class UnitUpToHalfGangLimitConstraint implements GangConstraint {

    private final String message;
    private final String unit;

    public UnitUpToHalfGangLimitConstraint(final String unit,
            final String message) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(message, "Received a null pointer as message");

        this.unit = unit;
        this.message = message;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final UnitUpToHalfGangLimitConstraint other;

        other = (UnitUpToHalfGangLimitConstraint) obj;
        return Objects.equals(unit, other.unit);
    }

    @Override
    public final String getErrorMessage() {
        return message;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unit);
    }

    @Override
    public final Boolean isValid(final Gang gang) {
        final Iterator<Unit> itr;
        Unit unit;
        Integer count;

        checkNotNull(gang, "Received a null pointer as gang");

        itr = gang.getUnits().iterator();
        count = 0;
        while (itr.hasNext()) {
            unit = itr.next();
            if (unit.getUnitName().equals(getUnit())) {
                count++;
            }
        }

        return (count <= (gang.getUnits().size() / 2));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("unit", unit).toString();
    }

    private final String getUnit() {
        return unit;
    }

}
