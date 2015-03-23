package com.wandrell.tabletop.punkapocalyptic.procedure.constraint;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.util.tag.GangAware;

public final class UnitUpToACountConstraint implements Constraint, GangAware {

    private final Integer count;
    private Gang          gang;
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
        return String.format(getMessage(), getUnit(), getCount());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unit, count);
    }

    @Override
    public final Boolean isValid() {
        final Predicate<Unit> isUnit;
        final Collection<Unit> units;

        checkNotNull(gang, "Validating a null gang");

        isUnit = new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit input) {
                return input.getName().equals(getUnit());
            }

        };

        units = Collections2.filter(getGang().getUnits(), isUnit);

        return (units.size() <= getCount());
    }

    @Override
    public final void setGang(final Gang gang) {
        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("unit", unit)
                .add("count", count).toString();
    }

    private final Integer getCount() {
        return count;
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
