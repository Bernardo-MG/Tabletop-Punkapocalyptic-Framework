package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.business.model.interval.DefaultInterval;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitArmorAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitMutationAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.WeaponOption;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.GroupedUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.mutation.Mutation;
import com.wandrell.tabletop.business.procedure.ConstraintValidator;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

public final class DefaultUnitConfigurationManager implements
        UnitConfigurationManager {

    private final Repository<UnitArmorAvailability>     armorAvaRepo;
    private final Repository<UnitEquipmentAvailability> equipAvaRepo;
    private final Repository<UnitMutationAvailability>  mutationAvaRepo;
    private final RulesetService                        rulesetService;
    private Unit                                        unit;
    private final ConstraintValidator                   validator;
    private final Repository<UnitWeaponAvailability>    weaponAvaRepo;

    public DefaultUnitConfigurationManager(final ConstraintValidator validator,
            final Repository<UnitArmorAvailability> armorAvaRepo,
            final Repository<UnitEquipmentAvailability> equipAvaRepo,
            final Repository<UnitMutationAvailability> mutationAvaRepo,
            final Repository<UnitWeaponAvailability> weaponAvaRepo,
            final RulesetService rulesetService) {
        super();

        checkNotNull(validator,
                "Received a null pointer as constraint validator");
        checkNotNull(armorAvaRepo,
                "Received a null pointer as the armor availability repository");
        checkNotNull(equipAvaRepo,
                "Received a null pointer as the equipment availability repository");
        checkNotNull(mutationAvaRepo,
                "Received a null pointer as the mutation availability repository");
        checkNotNull(weaponAvaRepo,
                "Received a null pointer as the weapon availability repository");
        checkNotNull(rulesetService,
                "Received a null pointer as the ruleset service");

        this.validator = validator;
        this.armorAvaRepo = armorAvaRepo;
        this.equipAvaRepo = equipAvaRepo;
        this.mutationAvaRepo = mutationAvaRepo;
        this.weaponAvaRepo = weaponAvaRepo;
        this.rulesetService = rulesetService;
    }

    @Override
    public final Interval getAllowedWeaponsInterval() {
        final Interval interval;
        final UnitWeaponAvailability ava;

        interval = new DefaultInterval();

        ava = getUnitWeaponAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        interval.setLowerLimit(ava.getMinWeapons());
        interval.setUpperLimit(ava.getMaxWeapons());

        return interval;
    }

    @Override
    public final Collection<Armor> getArmorOptions() {
        final Collection<Armor> armors;
        final UnitArmorAvailability ava;

        armors = new LinkedList<>();

        ava = getUnitArmorAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        if (ava.getInitialArmor() != null) {
            armors.add(ava.getInitialArmor());
            armors.addAll(ava.getArmorOptions());
        }

        return armors;
    }

    @Override
    public final Collection<Equipment> getEquipmentOptions() {
        final UnitEquipmentAvailability ava;

        ava = getUnitEquipmentAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        return ava.getEquipmentOptions();
    }

    @Override
    public final Integer getMaxMutations() {
        final UnitMutationAvailability ava;

        ava = getUnitMutationAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        return ava.getMaxMutations();
    }

    @Override
    public final Collection<Mutation> getMutations() {
        final UnitMutationAvailability ava;

        ava = getUnitMutationAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        return ava.getMutationOptions();
    }

    @Override
    public final Unit getUnit() {
        return unit;
    }

    @Override
    public final Collection<String> getValidationMessages() {
        return getConstraintValidator().getValidationMessages();
    }

    @Override
    public final Collection<WeaponEnhancement> getWeaponEnhancements(
            final Weapon weapon) {
        final WeaponOption option;
        final UnitWeaponAvailability ava;

        ava = getUnitWeaponAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        option = ava.getWeaponOptions().stream()
                .filter(o -> o.getWeapon().getName().equals(weapon.getName()))
                .iterator().next();

        return option.getEnhancements();
    }

    @Override
    public final Collection<Weapon> getWeaponOptions() {
        final Collection<Weapon> weapons;
        final UnitWeaponAvailability ava;

        ava = getUnitWeaponAvailabilityRepository()
                .getCollection(
                        a -> a.getUnit().getUnitName()
                                .equals(getUnit().getUnitName())).iterator()
                .next();

        weapons = ava.getWeaponOptions().stream().map(o -> o.getWeapon())
                .collect(Collectors.toList());

        return getRulesetService().filterWeaponOptions(getUnit().getWeapons(),
                weapons);
    }

    @Override
    public final Boolean isGrouped() {
        return (getUnit() instanceof GroupedUnit);
    }

    @Override
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;

        validate();
    }

    @Override
    public final Boolean validate() {
        return getConstraintValidator().validate();
    }

    private final ConstraintValidator getConstraintValidator() {
        return validator;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

    private final Repository<UnitArmorAvailability>
            getUnitArmorAvailabilityRepository() {
        return armorAvaRepo;
    }

    private final Repository<UnitEquipmentAvailability>
            getUnitEquipmentAvailabilityRepository() {
        return equipAvaRepo;
    }

    private final Repository<UnitMutationAvailability>
            getUnitMutationAvailabilityRepository() {
        return mutationAvaRepo;
    }

    private final Repository<UnitWeaponAvailability>
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
