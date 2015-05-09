package com.wandrell.tabletop.punkapocalyptic.procedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitWeapon;
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
    private Collection<UnitWeapon>                    unitWeapons;
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

        ava = getUnitArmorAvailabilityRepository().getAvailabilityForUnit(
                getUnitNameToken());

        armors = new LinkedList<>();
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

        equipment = new LinkedList<>();
        if (ava != null) {
            equipment.addAll(ava.getEquipmentOptions());
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

        mutations = new LinkedList<>();
        if (ava != null) {
            mutations.addAll(ava.getMutationOptions());
        }

        return mutations;
    }

    @Override
    public final Collection<WeaponEnhancement> getWeaponEnhancements(
            final UnitWeapon weapon) {
        final Collection<WeaponEnhancement> enhancements;

        enhancements = getUnitWeaponAvailabilityRepository()
                .getEnhancementsForUnitAndWeapon(getUnitNameToken(),
                        weapon.getTemplate().getName());

        return enhancements;
    }

    @Override
    public final Collection<UnitWeapon> getWeaponOptions() {
        final Collection<UnitWeapon> weaponOptions;
        final Collection<UnitWeapon> weapons;

        weaponOptions = getUnitWeaponAvailabilityRepository()
                .getAvailableWeaponsForUnit(getUnitNameToken());

        weapons = filterWeaponOptions(getUnitWeapons(), weaponOptions);

        return weapons;
    }

    public final void setUnitNameToken(final String name) {
        unitNameToken = name;
    }

    public final void setUnitWeapons(final Collection<UnitWeapon> weapons) {
        unitWeapons = weapons;
    }

    private final Collection<UnitWeapon> filterWeaponOptions(
            final Collection<UnitWeapon> weaponsHas,
            final Collection<UnitWeapon> weapons) {
        final Collection<UnitWeapon> weaponsFiltered;
        final Iterator<UnitWeapon> itrWeapons;
        UnitWeapon weapon;
        Boolean hasTwoHanded;

        // Checks if the unit has a two handed weapon
        hasTwoHanded = false;
        itrWeapons = weaponsHas.iterator();
        while ((!hasTwoHanded) && (itrWeapons.hasNext())) {
            weapon = itrWeapons.next();
            hasTwoHanded = weapon.getTemplate().isTwoHanded();
        }

        weaponsFiltered = new LinkedHashSet<>();
        for (final UnitWeapon w : weapons) {
            // Checks if the unit already has that weapon
            if (!weaponsHas.contains(w)) {
                if (w.getTemplate().isTwoHanded()) {
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

    private final Collection<UnitWeapon> getUnitWeapons() {
        return unitWeapons;
    }

}
