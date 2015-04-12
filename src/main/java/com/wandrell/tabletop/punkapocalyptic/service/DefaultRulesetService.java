package com.wandrell.tabletop.punkapocalyptic.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.repository.QueryableRepository;
import com.wandrell.tabletop.punkapocalyptic.conf.WeaponNameConf;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.MutantUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.util.tag.GangAware;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class DefaultRulesetService implements RulesetService {

    private Integer                                              bulletCost;
    private Integer                                              packMax;
    private final Map<Object, Object>                            rulesConfig;
    private final QueryableRepository<Weapon, Predicate<Weapon>> weaponRepo;

    public DefaultRulesetService(final Map<Object, Object> config,
            final QueryableRepository<Weapon, Predicate<Weapon>> weaponRepo) {
        super();

        checkNotNull(config, "Received a null pointer as config map");
        checkNotNull(weaponRepo,
                "Received a null pointer as weapons repository");

        rulesConfig = config;
        this.weaponRepo = weaponRepo;
    }

    @Override
    public final Collection<Weapon> filterWeaponOptions(
            final Collection<Weapon> weaponsHas,
            final Collection<Weapon> weapons) {
        final Collection<Weapon> weaponsFiltered;
        final Iterator<Weapon> itrWeapons;
        Weapon weapon;
        Boolean hasTwoHanded;

        // Checks if the unit has a two handed weapon
        hasTwoHanded = false;
        itrWeapons = weaponsHas.iterator();
        while ((!hasTwoHanded) && (itrWeapons.hasNext())) {
            weapon = itrWeapons.next();
            hasTwoHanded = weapon.isTwoHanded();
        }

        weaponsFiltered = new LinkedHashSet<>();
        for (final Weapon w : weapons) {
            // Checks if the unit already has that weapon
            if (!weaponsHas.contains(w)) {
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

        return weaponsFiltered;
    }

    @Override
    public final Integer getBulletCost() {
        if (bulletCost == null) {
            bulletCost = Integer.parseInt(getRulesConfiguration().get(
                    "bullet_cost").toString());
        }

        return bulletCost;
    }

    @Override
    public final Integer getGangValoration(final Gang gang) {
        Integer cost;

        cost = 0;
        for (final Unit unit : gang.getUnits()) {
            cost += unit.getValoration();
        }

        cost += (gang.getBullets() * getBulletCost());

        return cost;
    }

    @Override
    public final Integer getMaxAllowedUnits(final Gang gang) {
        final Integer step;
        final Integer range;
        Integer max;
        Integer value;

        // TODO: Maybe this should be loaded from a config file
        step = 3;
        range = 100;

        value = gang.getValoration();
        if (value == 0) {
            max = step;
        } else {
            max = 0;
            while (value > 0) {
                if (value > range) {
                    value -= range;
                } else {
                    value = 0;
                }
                max += step;
            }
        }

        return max;
    }

    @Override
    public final Integer getPackMaxSize() {
        if (packMax == null) {
            packMax = Integer.parseInt(getRulesConfiguration().get(
                    "pack_max_size").toString());
        }

        return packMax;
    }

    @Override
    public final MeleeWeapon getTwoHandedMeleeEquivalent() {
        // TODO: Don't query the repository here
        return (MeleeWeapon) getWeaponRepository().getEntity(
                new Predicate<Weapon>() {

                    @Override
                    public final boolean apply(final Weapon input) {
                        return input.getName().equals(
                                WeaponNameConf.IMPROVISED_WEAPON);
                    }

                });
    }

    @Override
    public final Integer getUnitValoration(final Unit unit) {
        Integer valoration;

        valoration = unit.getBaseCost();

        for (final Weapon weapon : unit.getWeapons()) {
            valoration += weapon.getCost();
        }

        for (final Equipment equipment : unit.getEquipment()) {
            valoration += equipment.getCost();
        }

        if (unit.getArmor() != null) {
            valoration += unit.getArmor().getCost();
        }

        if (unit instanceof MutantUnit) {
            for (final Mutation mutation : ((MutantUnit) unit).getMutations()) {
                valoration += mutation.getCost();
            }
        }

        if (unit instanceof GroupedUnit) {
            valoration = valoration
                    * ((GroupedUnit) unit).getGroupSize().getValue();
        }

        return valoration;
    }

    @Override
    public final void setUpMaxUnitsValueHandler(final ValueBox value,
            final Gang gang) {
        if ((value instanceof ValueBox) && (value instanceof GangAware)) {
            ((GangAware) value).setGang(gang);
        }
    }

    private final Map<Object, Object> getRulesConfiguration() {
        return rulesConfig;
    }

    private final QueryableRepository<Weapon, Predicate<Weapon>>
            getWeaponRepository() {
        return weaponRepo;
    }

}
