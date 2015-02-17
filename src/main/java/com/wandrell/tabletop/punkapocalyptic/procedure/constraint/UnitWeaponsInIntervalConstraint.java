package com.wandrell.tabletop.punkapocalyptic.procedure.constraint;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.util.tag.UnitAware;

public final class UnitWeaponsInIntervalConstraint implements Constraint,
        UnitAware {

    private String                                   formattedMessage;
    private final String                             message;
    private Unit                                     unit;
    private final Repository<UnitWeaponAvailability> weaponAvaRepo;

    public UnitWeaponsInIntervalConstraint(
            final Repository<UnitWeaponAvailability> repo, final String message) {
        super();

        // TODO: Is this really needed?

        checkNotNull(repo,
                "Received a null pointer as unit weapon availability repository");
        checkNotNull(message, "Received a null pointer as message");

        weaponAvaRepo = repo;
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
        return formattedMessage;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass().getName());
    }

    @Override
    public final Boolean isValid() {
        final Boolean valid;
        final Integer weaponsCount;
        final UnitWeaponAvailability ava;

        checkNotNull(unit, "Validating a null unit");

        ava = getUnitWeaponAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        weaponsCount = getUnit().getWeapons().size();

        valid = ((weaponsCount >= ava.getMinWeapons()) && (weaponsCount <= ava
                .getMaxWeapons()));

        if (!valid) {
            formattedMessage = String.format(getMessage(), ava.getMinWeapons(),
                    ava.getMaxWeapons());
        }

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

    private final String getMessage() {
        return message;
    }

    private final Unit getUnit() {
        return unit;
    }

    private final Repository<UnitWeaponAvailability>
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
