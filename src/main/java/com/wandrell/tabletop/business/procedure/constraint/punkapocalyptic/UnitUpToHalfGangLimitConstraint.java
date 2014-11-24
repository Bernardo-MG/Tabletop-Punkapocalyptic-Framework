package com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.GangAware;

public final class UnitUpToHalfGangLimitConstraint implements Constraint,
        GangAware {

    private Gang         gang;
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
        return String.format(getMessage(), getUnit());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unit);
    }

    @Override
    public final Boolean isValid() {
        final Predicate<Unit> isUnit;
        final Collection<Unit> units;

        checkNotNull(getGang(), "Validating a null gang");

        isUnit = (final Unit u) -> u.getUnitName().equals(getUnit());

        units = getGang().getUnits().stream().filter(isUnit)
                .collect(Collectors.toList());

        return (units.size() <= (getGang().getUnits().size() / 2));
    }

    @Override
    public final void setGang(final Gang gang) {
        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("unit", unit).toString();
    }

    private final Gang getGang() {
        return gang;
    }

    private final String getMessage() {
        return message;
    }

    private final String getUnit() {
        return unit;
    }

}
