package com.wandrell.tabletop.punkapocalyptic.procedure.constraint;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitWeaponAvailabilityRepository;

public final class UnitWeaponsInIntervalConstraint implements Constraint {

    private String                                 formattedMessage;
    private final String                           message;
    private final Unit                             unit;
    private final UnitWeaponAvailabilityRepository weaponAvaRepo;

    public UnitWeaponsInIntervalConstraint(final Unit unit,
            final UnitWeaponAvailabilityRepository repo, final String message) {
        super();

        // TODO: Is this really needed?

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(repo,
                "Received a null pointer as unit weapon availability repository");
        checkNotNull(message, "Received a null pointer as message");

        this.unit = unit;
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
    public final String getName() {
        return "unit_weapon_interval";
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

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnit());

        if (ava == null) {
            valid = false;
        } else {
            weaponsCount = getUnit().getWeapons().size();

            valid = ((weaponsCount >= ava.getMinWeapons()) && (weaponsCount <= ava
                    .getMaxWeapons()));

            if (!valid) {
                formattedMessage = String.format(getMessage(),
                        ava.getMinWeapons(), ava.getMaxWeapons());
            }
        }

        return valid;
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

    private final UnitWeaponAvailabilityRepository
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
