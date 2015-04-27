package com.wandrell.tabletop.punkapocalyptic.procedure;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.procedure.DefaultConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitWeaponsInIntervalConstraint;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitArmorAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitEquipmentAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitMutationAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitWeaponAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class DefaultUnitConfigurationManager implements
        UnitConfigurationManager {

    private final UnitArmorAvailabilityRepository     armorAvaRepo;
    private final String                              constraintMessage;
    private final UnitEquipmentAvailabilityRepository equipAvaRepo;
    private final UnitMutationAvailabilityRepository  mutationAvaRepo;
    private final RulesetService                      rulesetService;
    private Unit                                      unit;
    private final ConstraintValidator                 validator;
    private final UnitWeaponAvailabilityRepository    weaponAvaRepo;

    public DefaultUnitConfigurationManager(final String constraintMessage,
            final UnitArmorAvailabilityRepository armorAvaRepo,
            final UnitEquipmentAvailabilityRepository equipAvaRepo,
            final UnitMutationAvailabilityRepository mutationAvaRepo,
            final UnitWeaponAvailabilityRepository weaponAvaRepo,
            final RulesetService rulesetService) {
        super();

        checkNotNull(constraintMessage,
                "Received a null pointer as the constraint message");
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

        this.constraintMessage = constraintMessage;

        this.validator = new DefaultConstraintValidator();
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

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnit().getUnitTemplate().getNameToken());

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
                getUnit().getUnitTemplate().getNameToken());

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
                getUnit().getUnitTemplate().getNameToken());

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
                getUnit().getUnitTemplate().getNameToken());

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
                getUnit().getUnitTemplate().getNameToken());

        if (ava == null) {
            mutations = new LinkedList<>();
        } else {
            mutations = ava.getMutationOptions();
        }

        return mutations;
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
        final Collection<WeaponEnhancement> enhancements;

        ava = getUnitWeaponAvailabilityRepository().getAvailabilityForUnit(
                getUnit().getUnitTemplate().getNameToken());

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
                getUnit().getUnitTemplate().getNameToken());

        if (ava == null) {
            weapons = new LinkedList<>();
        } else {
            weaponOptions = new LinkedList<>();
            for (final WeaponOption option : ava.getWeaponOptions()) {
                weaponOptions.add(option.getWeapon());
            }
            weapons = getRulesetService().filterWeaponOptions(
                    getUnit().getWeapons(), weaponOptions);
        }

        return weapons;
    }

    @Override
    public final Boolean isGrouped() {
        return (getUnit() instanceof GroupedUnit);
    }

    @Override
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;

        getConstraintValidator().clearConstraints();

        getConstraintValidator().addConstraint(
                new UnitWeaponsInIntervalConstraint(unit,
                        getUnitWeaponAvailabilityRepository(),
                        getConstraintMessage()));

        validate();
    }

    @Override
    public final Boolean validate() {
        return getConstraintValidator().validate();
    }

    private final String getConstraintMessage() {
        return constraintMessage;
    }

    private final ConstraintValidator getConstraintValidator() {
        return validator;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
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

    private final UnitWeaponAvailabilityRepository
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
