package com.wandrell.tabletop.punkapocalyptic.service.ruleset.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;

public final class FilterWeaponOptionsCommand implements
        ReturnCommand<Collection<Weapon>> {

    private final Collection<Weapon> weapons;
    private Collection<Weapon>       weaponsFiltered;
    private final Collection<Weapon> weaponsHas;

    public FilterWeaponOptionsCommand(final Collection<Weapon> weaponsHas,
            final Collection<Weapon> weapons) {
        super();

        this.weaponsHas = weaponsHas;
        this.weapons = weapons;
    }

    @Override
    public final void execute() throws Exception {
        final Iterator<Weapon> itrWeapons;
        Weapon weapon;
        Boolean hasTwoHanded;

        // Checks if the unit has a two handed weapon
        hasTwoHanded = false;
        itrWeapons = getUnitWeapons().iterator();
        while ((!hasTwoHanded) && (itrWeapons.hasNext())) {
            weapon = itrWeapons.next();
            hasTwoHanded = weapon.isTwoHanded();
        }

        weaponsFiltered = new LinkedHashSet<>();
        for (final Weapon w : getWeapons()) {
            // Checks if the unit already has that weapon
            if (!getUnitWeapons().contains(w)) {
                if (w.isTwoHanded()) {
                    // If it is two handed
                    // Then the unit should have no 2h weapon
                    if (!hasTwoHanded) {
                        weaponsFiltered.add(w);
                    }
                } else {
                    weaponsFiltered.add(w);
                }
            }
        }
    }

    @Override
    public final Collection<Weapon> getResult() {
        return weaponsFiltered;
    }

    private final Collection<Weapon> getUnitWeapons() {
        return weaponsHas;
    }

    private final Collection<Weapon> getWeapons() {
        return weapons;
    }

}
