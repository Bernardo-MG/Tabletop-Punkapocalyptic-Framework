package com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic;

import static com.google.common.base.Preconditions.checkArgument;
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

public final class DependantUnitConstraint implements Constraint, GangAware {

    private final Integer count;
    private Gang          gang;
    private final String  mainUnit;
    private final String  message;
    private final String  unit;

    public DependantUnitConstraint(final String mainUnit, final String unit,
            final Integer count, final String message) {
        super();

        checkNotNull(mainUnit, "Received a null pointer as main unit");
        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(count, "Received a null pointer as count");
        checkNotNull(message, "Received a null pointer as message");

        checkArgument(count >= 0, "The count should be positive or zero");

        this.mainUnit = mainUnit;
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

        final DependantUnitConstraint other;

        other = (DependantUnitConstraint) obj;
        return Objects.equals(unit, other.unit)
                && Objects.equals(count, other.count);
    }

    @Override
    public final String getErrorMessage() {
        return String.format(getMessage(), getUnit(), getCount());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unit, count);
    }

    @Override
    public final Boolean isValid() {
        final Predicate<Unit> hasMainUnit;
        final Predicate<Unit> hasUnit;
        final Collection<Unit> units;
        final Collection<Unit> mainUnits;

        checkNotNull(gang, "Validating a null gang");

        hasUnit = (final Unit u) -> u.getUnitName().equals(getUnit());
        hasMainUnit = (final Unit u) -> u.getUnitName().equals(getMainUnit());

        units = getGang().getUnits().stream().filter(hasUnit)
                .collect(Collectors.toList());
        mainUnits = getGang().getUnits().stream().filter(hasMainUnit)
                .collect(Collectors.toList());

        return (units.size() <= (mainUnits.size() * getCount()));
    }

    @Override
    public final void setGang(final Gang gang) {
        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("mainUnit", mainUnit)
                .add("unit", unit).add("count", count).toString();
    }

    private final Integer getCount() {
        return count;
    }

    private final Gang getGang() {
        return gang;
    }

    private final String getMainUnit() {
        return mainUnit;
    }

    private final String getMessage() {
        return message;
    }

    private final String getUnit() {
        return unit;
    }

}
