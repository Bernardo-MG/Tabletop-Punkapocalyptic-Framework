package com.wandrell.tabletop.punkapocalyptic.procedure;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.procedure.DefaultConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitWeaponsInIntervalConstraint;
import com.wandrell.tabletop.punkapocalyptic.repository.MutationRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitArmorAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitEquipmentAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitMutationAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.repository.UnitWeaponAvailabilityRepository;

public final class DefaultUnitConfigurationManager implements
        UnitConfigurationManager {

    private final DefaultUnitConfigurationOptions  confOptions;
    private final String                           constraintMessage;
    private Unit                                   unit;
    private final ConstraintValidator              validator;
    private final UnitWeaponAvailabilityRepository weaponAvaRepo;

    public DefaultUnitConfigurationManager(final String constraintMessage,
            final MutationRepository mutationRepo,
            final UnitArmorAvailabilityRepository armorAvaRepo,
            final UnitEquipmentAvailabilityRepository equipAvaRepo,
            final UnitMutationAvailabilityRepository mutationAvaRepo,
            final UnitWeaponAvailabilityRepository weaponAvaRepo) {
        super();

        checkNotNull(constraintMessage,
                "Received a null pointer as the constraint message");
        checkNotNull(mutationRepo,
                "Received a null pointer as the mutations repository");
        checkNotNull(armorAvaRepo,
                "Received a null pointer as the armor availability repository");
        checkNotNull(equipAvaRepo,
                "Received a null pointer as the equipment availability repository");
        checkNotNull(mutationAvaRepo,
                "Received a null pointer as the mutation availability repository");
        checkNotNull(weaponAvaRepo,
                "Received a null pointer as the weapon availability repository");

        this.constraintMessage = constraintMessage;

        this.validator = new DefaultConstraintValidator();

        confOptions = new DefaultUnitConfigurationOptions(mutationRepo,
                armorAvaRepo, equipAvaRepo, mutationAvaRepo, weaponAvaRepo);

        this.weaponAvaRepo = weaponAvaRepo;
    }

    @Override
    public final DefaultUnitConfigurationOptions getOptions() {
        return confOptions;
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
    public final Boolean isGrouped() {
        return (getUnit() instanceof DefaultGroupedUnit);
    }

    @Override
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;

        getOptions().setUnitNameToken(
                getUnit().getUnitTemplate().getNameToken());
        getOptions().setUnitWeapons(getUnit().getWeapons());

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

    private final UnitWeaponAvailabilityRepository
            getUnitWeaponAvailabilityRepository() {
        return weaponAvaRepo;
    }

}
