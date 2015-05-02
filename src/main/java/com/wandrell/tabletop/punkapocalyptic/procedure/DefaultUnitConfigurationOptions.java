package com.wandrell.tabletop.punkapocalyptic.procedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitArmorAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitEquipmentAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitMutationAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitWeaponAvailabilityRepository;

public final class DefaultUnitConfigurationOptions implements
        UnitConfigurationOptions {

    private final UnitArmorAvailabilityRepository     armorAvaRepo;
    private final UnitEquipmentAvailabilityRepository equipAvaRepo;
    private final UnitMutationAvailabilityRepository  mutationAvaRepo;
    private String                                    unitNameToken;
    private Collection<Weapon>                        unitWeapons;
    private final UnitWeaponAvailabilityRepository    weaponAvaRepo;

    public DefaultUnitConfigurationOptions(
            final UnitArmorAvailabilityRepository armorAvaRepo,
            final UnitEquipmentAvailabilityRepository equipAvaRepo,
            final UnitMutationAvailabilityRepository mutationAvaRepo,
            final UnitWeaponAvailabilityRepository weaponAvaRepo) {
        super();

        this.armorAvaRepo = armorAvaRepo;
        this.equipAvaRepo = equipAvaRepo;
        this.mutationAvaRepo = mutationAvaRepo;
        this.weaponAvaRepo = weaponAvaRepo;
    }

    @Override
    public final Interval getAllowedWeaponsInterval() {
        final Interval interval;
        final UnitWeaponAvailability ava;

        interval = new DefaultInterval();

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            interval.setLowerLimit(0);
            interval.setUpperLimit(0);
        } else {
            interval.setLowerLimit(ava.getMinWeapons());
            interval.setUpperLimit(ava.getMaxWeapons());
        }

        return interval;
    }

    @Override
    public final Collection<ArmorOption> getArmorOptions() {
        final Collection<ArmorOption> armors;
        final UnitArmorAvailability ava;

        armors = new LinkedList<>();

        ava = getUnitArmorAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava.getInitialArmor() != null) {
            armors.add(ava.getInitialArmor());
            armors.addAll(ava.getArmorOptions());
        }

        return armors;
    }

    @Override
    public final Collection<Equipment> getEquipmentOptions() {
        final UnitEquipmentAvailability ava;
        final Collection<Equipment> equipment;

        ava = getUnitEquipmentAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            equipment = new LinkedList<>();
        } else {
            equipment = ava.getEquipmentOptions();
        }

        return equipment;
    }

    @Override
    public final Integer getMaxMutations() {
        final UnitMutationAvailability ava;
        final Integer max;

        ava = getUnitMutationAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            max = 0;
        } else {
            max = ava.getMaxMutations();
        }

        return max;
    }

    @Override
    public final Collection<Mutation> getMutations() {
        final UnitMutationAvailability ava;
        final Collection<Mutation> mutations;

        ava = getUnitMutationAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            mutations = new LinkedList<>();
        } else {
            mutations = ava.getMutationOptions();
        }

        return mutations;
    }

    @Override
    public final Collection<WeaponEnhancement> getWeaponEnhancements(
            final Weapon weapon) {
        final WeaponOption option;
        final UnitWeaponAvailability ava;
        final Collection<WeaponEnhancement> enhancements;

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            enhancements = new LinkedList<>();
        } else {
            option = Collections2
                    .filter(ava.getWeaponOptions(),
                            new Predicate<WeaponOption>() {

                                @Override
                                public final boolean apply(
                                        final WeaponOption input) {
                                    return input.getWeapon().getName()
                                            .equals(weapon.getName());
                                }

                            }).iterator().next();

            enhancements = option.getEnhancements();
        }

        return enhancements;
    }

    @Override
    public final Collection<Weapon> getWeaponOptions() {
        final Collection<Weapon> weaponOptions;
        final Collection<Weapon> weapons;
        final UnitWeaponAvailability ava;

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        if (ava == null) {
            weapons = new LinkedList<>();
        } else {
            weaponOptions = new LinkedList<>();
            for (final WeaponOption option : ava.getWeaponOptions()) {
                weaponOptions.add(option.getWeapon());
            }
            weapons = filterWeaponOptions(getUnitWeapons(), weaponOptions);
        }

        return weapons;
    }

    public final void setUnitNameToken(final String name) {
        unitNameToken = name;
    }

    public final void setUnitWeapons(final Collection<Weapon> weapons) {
        unitWeapons = weapons;
    }

    private final Collection<Weapon> filterWeaponOptions(
            final Collection<Weapon> weaponsHas,
            final Collection<Weapon> weapons) {
        final Collection<Weapon> weaponsFiltered;
        final Iterator<Weapon> itrWeapons;
        Weapon weapon;
        Boolean hasTwoHanded;

        // TODO: This method should not be part of the service

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

    private final UnitArmorAvailabilityRepository
            getUnitArmorAvailabilityRepository() {
        return armorAvaRepo;
    }

    private final UnitEquipmentAvailabilityRepository
            getUnitEquipmentAvailabilityRepository() {
        return equipAvaRepo;
    }

    private final UnitMutationAvailabilityRepository
            getUnitMutationAvailabilityRepository() {
        return mutationAvaRepo;
    }

    private final String getUnitNameToken() {
        return unitNameToken;
    }

    private final UnitWeaponAvailabilityRepository
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

    private final Collection<Weapon> getUnitWeapons() {
        return unitWeapons;
    }

}
