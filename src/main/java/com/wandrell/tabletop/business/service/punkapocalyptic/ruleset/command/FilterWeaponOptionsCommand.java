package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.command.ReturnCommand;

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
        final Collection<Weapon> weapons;
        Weapon weapon;
        Boolean hasTwoHanded;

        hasTwoHanded = false;
        itrWeapons = getUnitWeapons().iterator();
        while ((!hasTwoHanded) && (itrWeapons.hasNext())) {
            weapon = itrWeapons.next();
            hasTwoHanded = weapon.isTwoHanded();
        }

        result = new LinkedHashSet<>();
        weapons = getUnitWeapons();
        for (final Weapon w : getWeapons()) {
            if (!weapons.contains(w)) {
                if (hasTwoHanded) {
                    if (!w.isTwoHanded()) {
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
