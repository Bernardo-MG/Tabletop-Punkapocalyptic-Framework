package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import java.util.Collection;
import java.util.LinkedList;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.procedure.punkapocalyptic.UnitConfigurationController;

public final class DefaultUnitConfigurationController implements
        UnitConfigurationController {

    private final String     compulsoryWeaponsError;
    private AvailabilityUnit unit;

    public DefaultUnitConfigurationController(
            final String compulsoryWeaponsError) {
        super();

        this.compulsoryWeaponsError = compulsoryWeaponsError;
    }

    @Override
    public final String getFailedConstraintsText() {
        String error;

        error = "";

        if (getUnit().getWeapons().size() < getUnit().getMinWeapons()) {
            error = String.format(getCompulsoryWeaponsError(), getUnit()
                    .getMinWeapons());
        }

        return error;
    }

    @Override
    public final Collection<Weapon> getWeaponsSelection() {
        final Collection<Weapon> weapons;

        weapons = new LinkedList<>();
        for (final Weapon weapon : getUnit().getWeaponOptions()) {
            if ((!weapons.contains(weapon))
                    && (weapon.getHands() <= getUnit().getFreeWeaponSlots()
                            .getStoredValue())) {
                weapons.add(weapon);
            }
        }

        return weapons;
    }

    @Override
    public final Boolean isFinished() {
        return ((getUnit() != null) && (getUnit().getWeapons().size() >= getUnit()
                .getMinWeapons()));
    }

    @Override
    public final void setUnit(final AvailabilityUnit unit) {
        this.unit = unit;
    }

    protected final String getCompulsoryWeaponsError() {
        return compulsoryWeaponsError;
    }

    protected final AvailabilityUnit getUnit() {
        return unit;
    }

}
