package com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.business.model.procedure.constraint.punkapocalyptic.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;

public final class UnitUpToACountConstraint implements GangConstraint {

    private final Integer count;
    private final String  message;
    private final String  unit;

    public UnitUpToACountConstraint(final String unit, final Integer count,
            final String message) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(count, "Received a null pointer as count");
        checkNotNull(message, "Received a null pointer as message");

        checkArgument(count >= 0, "The count should be positive or zero");

        this.unit = unit;
        this.count = count;
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

        final UnitUpToACountConstraint other;

        other = (UnitUpToACountConstraint) obj;
        return Objects.equals(unit, other.unit)
                && Objects.equals(count, other.count);
    }

    @Override
    public final String getErrorMessage() {
        return message;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unit, count);
    }

    @Override
    public final Boolean isValid(final Gang gang) {
        final Predicate<Unit> isUnit;
        final Collection<Unit> units;

        checkNotNull(gang, "Received a null pointer as gang");

        isUnit = (final Unit u) -> u.getUnitName().equals(getUnit());

        units = gang.getUnits().stream().filter(isUnit)
                .collect(Collectors.toList());

        return (units.size() <= getCount());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("unit", unit)
                .add("count", count).toString();
    }

    private final Integer getCount() {
        return count;
    }

    private final String getUnit() {
        return unit;
    }

}
