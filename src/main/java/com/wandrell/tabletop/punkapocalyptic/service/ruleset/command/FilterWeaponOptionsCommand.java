package com.wandrell.tabletop.punkapocalyptic.service.ruleset.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;

public final class FilterWeaponOptionsCommand implements
        ReturnCommand<Collection<Weapon>> {

    private final Collection<Weapon> weapons;
    private final Collection<Weapon> weaponsHas;

    public FilterWeaponOptionsCommand(final Collection<Weapon> weaponsHas,
            final Collection<Weapon> weapons) {
        super();

        this.weaponsHas = weaponsHas;
        this.weapons = weapons;
    }

    @Override
    public final Collection<Weapon> execute() throws Exception {
        final Collection<Weapon> result;
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

        result = new LinkedHashSet<>();
        for (final Weapon w : getWeapons()) {
            // Checks if the unit already has that weapon
            if (!getUnitWeapons().contains(w)) {
                if (w.isTwoHanded()) {
                    // If it is two handed
                    // Then the unit should have no 2h weapon
                    if (!hasTwoHanded) {
                        result.add(w);
                    }
                } else {
                    result.add(w);
                }
            }
        }

        return result;
    }

    private final Collection<Weapon> getUnitWeapons() {
        return weaponsHas;
    }

    private final Collection<Weapon> getWeapons() {
        return weapons;
    }

}
