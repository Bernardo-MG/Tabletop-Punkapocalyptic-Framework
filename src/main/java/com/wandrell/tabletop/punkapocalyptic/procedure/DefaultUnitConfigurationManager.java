package com.wandrell.tabletop.punkapocalyptic.procedure;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.pattern.repository.QueryableRepository;
import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.procedure.DefaultConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitWeaponsInIntervalConstraint;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class DefaultUnitConfigurationManager implements
        UnitConfigurationManager {

    private final QueryableRepository<UnitArmorAvailability, Predicate<UnitArmorAvailability>>         armorAvaRepo;
    private final String                                                                               constraintMessage;
    private final QueryableRepository<UnitEquipmentAvailability, Predicate<UnitEquipmentAvailability>> equipAvaRepo;
    private final QueryableRepository<UnitMutationAvailability, Predicate<UnitMutationAvailability>>   mutationAvaRepo;
    private final RulesetService                                                                       rulesetService;
    private Unit                                                                                       unit;
    private final ConstraintValidator                                                                  validator;
    private final QueryableRepository<UnitWeaponAvailability, Predicate<UnitWeaponAvailability>>       weaponAvaRepo;

    public DefaultUnitConfigurationManager(
            final String constraintMessage,
            final QueryableRepository<UnitArmorAvailability, Predicate<UnitArmorAvailability>> armorAvaRepo,
            final QueryableRepository<UnitEquipmentAvailability, Predicate<UnitEquipmentAvailability>> equipAvaRepo,
            final QueryableRepository<UnitMutationAvailability, Predicate<UnitMutationAvailability>> mutationAvaRepo,
            final QueryableRepository<UnitWeaponAvailability, Predicate<UnitWeaponAvailability>> weaponAvaRepo,
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
        final Collection<UnitWeaponAvailability> avas;

        interval = new DefaultInterval();

        avas = getUnitWeaponAvailabilityRepository().getCollection(
                new Predicate<UnitWeaponAvailability>() {

                    @Override
                    public boolean apply(UnitWeaponAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            ava = avas.iterator().next();

            interval.setLowerLimit(ava.getMinWeapons());
            interval.setUpperLimit(ava.getMaxWeapons());
        } else {
            interval.setLowerLimit(0);
            interval.setUpperLimit(0);
        }

        return interval;
    }

    @Override
    public final Collection<Armor> getArmorOptions() {
        final Collection<Armor> armors;
        final UnitArmorAvailability ava;
        final Collection<UnitArmorAvailability> avas;

        armors = new LinkedList<>();

        avas = getUnitArmorAvailabilityRepository().getCollection(
                new Predicate<UnitArmorAvailability>() {

                    @Override
                    public boolean apply(UnitArmorAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            ava = avas.iterator().next();

            if (ava.getInitialArmor() != null) {
                armors.add(ava.getInitialArmor());
                armors.addAll(ava.getArmorOptions());
            }
        }

        return armors;
    }

    @Override
    public final Collection<Equipment> getEquipmentOptions() {
        final UnitEquipmentAvailability ava;
        final Collection<UnitEquipmentAvailability> avas;
        final Collection<Equipment> equipment;

        avas = getUnitEquipmentAvailabilityRepository().getCollection(
                new Predicate<UnitEquipmentAvailability>() {

                    @Override
                    public boolean apply(UnitEquipmentAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            ava = avas.iterator().next();
            equipment = ava.getEquipmentOptions();
        } else {
            equipment = new LinkedList<>();
        }

        return equipment;
    }

    @Override
    public final Integer getMaxMutations() {
        final UnitMutationAvailability ava;
        final Collection<UnitMutationAvailability> avas;
        final Integer max;

        avas = getUnitMutationAvailabilityRepository().getCollection(
                new Predicate<UnitMutationAvailability>() {

                    @Override
                    public boolean apply(UnitMutationAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            ava = avas.iterator().next();
            max = ava.getMaxMutations();
        } else {
            max = 0;
        }

        return max;
    }

    @Override
    public final Collection<Mutation> getMutations() {
        final Collection<UnitMutationAvailability> avas;
        final Collection<Mutation> mutations;

        avas = getUnitMutationAvailabilityRepository().getCollection(
                new Predicate<UnitMutationAvailability>() {

                    @Override
                    public boolean apply(UnitMutationAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });
        if (!avas.isEmpty()) {
            mutations = avas.iterator().next().getMutationOptions();
        } else {
            mutations = new LinkedList<>();
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
        final Collection<UnitWeaponAvailability> avas;
        final Collection<WeaponEnhancement> enhancements;

        avas = getUnitWeaponAvailabilityRepository().getCollection(
                new Predicate<UnitWeaponAvailability>() {

                    @Override
                    public boolean apply(UnitWeaponAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            ava = avas.iterator().next();

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
        } else {
            enhancements = new LinkedList<>();
        }

        return enhancements;
    }

    @Override
    public final Collection<Weapon> getWeaponOptions() {
        final Collection<Weapon> weaponOptions;
        final Collection<Weapon> weapons;
        final Collection<UnitWeaponAvailability> avas;

        avas = getUnitWeaponAvailabilityRepository().getCollection(
                new Predicate<UnitWeaponAvailability>() {

                    @Override
                    public boolean apply(UnitWeaponAvailability input) {
                        return input.getUnit().getName()
                                .equals(getUnit().getName());
                    }

                });

        if (!avas.isEmpty()) {
            weaponOptions = new LinkedList<>();
            for (final UnitWeaponAvailability avai : avas) {
                for (final WeaponOption option : avai.getWeaponOptions()) {
                    weaponOptions.add(option.getWeapon());
                }
            }
            weapons = getRulesetService().filterWeaponOptions(
                    getUnit().getWeapons(), weaponOptions);
        } else {
            weapons = new LinkedList<>();
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

    private final
            QueryableRepository<UnitArmorAvailability, Predicate<UnitArmorAvailability>>
            getUnitArmorAvailabilityRepository() {
        return armorAvaRepo;
    }

    private final
            QueryableRepository<UnitEquipmentAvailability, Predicate<UnitEquipmentAvailability>>
            getUnitEquipmentAvailabilityRepository() {
        return equipAvaRepo;
    }

    private final
            QueryableRepository<UnitMutationAvailability, Predicate<UnitMutationAvailability>>
            getUnitMutationAvailabilityRepository() {
        return mutationAvaRepo;
    }

    private final
            QueryableRepository<UnitWeaponAvailability, Predicate<UnitWeaponAvailability>>
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
