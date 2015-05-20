package com.wandrell.tabletop.punkapocalyptic.procedure.constraint;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.stats.valuebox.ValueBox;

public final class GangUnitsUpToLimitConstraint implements Constraint {

    private final Gang     gang;
    private final String   message;
    private final ValueBox unitsLimit;

    public GangUnitsUpToLimitConstraint(final Gang gang,
            final ValueBox unitsLimit, final String message) {
        super();

        checkNotNull(gang, "Received a null pointer as gang");
        checkNotNull(unitsLimit, "Received a null pointer as units limit");
        checkNotNull(message, "Received a null pointer as message");

        this.gang = gang;
        this.unitsLimit = unitsLimit;
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
        GangUnitsUpToLimitConstraint other = (GangUnitsUpToLimitConstraint) obj;
        return Objects.equals(unitsLimit, other.unitsLimit);
    }

    @Override
    public final String getErrorMessage() {
        return String.format(getMessage(), getGang().getUnits().size(),
                getUnitsLimit());
    }

    @Override
    public final String getName() {
        return "gang_up_to_limit";
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName(), unitsLimit);
    }

    @Override
    public final Boolean isValid() {
        checkNotNull(gang, "Validating a null gang");
        checkArgument(getUnitsLimit() >= 0,
                "The limit should be positive or zero");

        return (getGang().getUnits().size() <= getUnitsLimit());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("limit", unitsLimit)
                .toString();
    }

    private final Gang getGang() {
        return gang;
    }

    private final String getMessage() {
        return message;
    }

    private final Integer getUnitsLimit() {
        return unitsLimit.getValue();
    }

}
