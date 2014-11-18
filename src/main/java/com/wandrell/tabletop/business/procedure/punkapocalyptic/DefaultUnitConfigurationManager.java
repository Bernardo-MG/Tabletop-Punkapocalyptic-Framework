package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitArmorAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.procedure.ConstraintValidator;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultUnitConfigurationManager implements
        UnitConfigurationManager {

    private final DataModelService    dataModelService;
    private final RulesetService      rulesetService;
    private Unit                      unit;
    private final ConstraintValidator validator;

    public DefaultUnitConfigurationManager(final ConstraintValidator validator,
            final DataModelService dataModelService,
            final RulesetService rulesetService) {
        super();

        checkNotNull(validator,
                "Received a null pointer as constraint validator");
        checkNotNull(dataModelService,
                "Received a null pointer as the data model service");
        checkNotNull(rulesetService,
                "Received a null pointer as the ruleset service");

        this.validator = validator;
        this.dataModelService = dataModelService;
        this.rulesetService = rulesetService;
    }

    @Override
    public final Interval getAllowedWeaponsInterval() {
        return getDataModelService().getUnitAllowedWeaponsInterval(
                getUnit().getUnitName());
    }

    @Override
    public final Collection<Armor> getArmorOptions() {
        final UnitArmorAvailability availability;
        final Collection<Armor> armors;

        armors = new LinkedList<>();

        availability = getDataModelService().getUnitArmorAvailability(
                getUnit().getUnitName());

        armors.add(availability.getInitialArmor());
        armors.addAll(availability.getArmorOptions());

        return armors;
    }

    @Override
    public final Collection<Equipment> getEquipmentOptions() {
        return getDataModelService().getEquipmentOptions(
                getUnit().getUnitName());
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
        return getDataModelService().getWeaponEnhancements(
                getUnit().getUnitName(), weapon.getName());
    }

    @Override
    public final Collection<Weapon> getWeaponOptions() {
        final Collection<Weapon> weapons;

        weapons = getDataModelService().getWeaponOptions(
                getUnit().getUnitName());

        return getRulesetService().filterWeaponOptions(getUnit().getWeapons(),
                weapons);
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

    private final DataModelService getDataModelService() {
        return dataModelService;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

}
