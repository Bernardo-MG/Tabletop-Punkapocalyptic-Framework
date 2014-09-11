package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultAvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.UnitDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.UnitDAO;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllUnitsAvailabilityCommand implements
        ReturnCommand<Map<String, AvailabilityUnit>>, UnitDAOAware {

    private final Map<String, Armor>                                 armor;
    private final Map<String, Collection<Armor>>                     armors;
    private final Map<String, Collection<ArmyBuilderUnitConstraint>> constraints;
    private UnitDAO                                                  daoUnit;
    private final Map<String, Interval>                              weaponIntervals;
    private final Map<String, Collection<Weapon>>                    weapons;

    public GetAllUnitsAvailabilityCommand(final Map<String, Armor> armor,
            final Map<String, Collection<Armor>> armors,
            final Map<String, Collection<Weapon>> weapons,
            final Map<String, Interval> weaponIntervals,
            final Map<String, Collection<ArmyBuilderUnitConstraint>> constraints) {
        super();

        this.armor = armor;
        this.armors = armors;
        this.weapons = weapons;
        this.weaponIntervals = weaponIntervals;
        this.constraints = constraints;
    }

    @Override
    public final Map<String, AvailabilityUnit> execute() {
        final Map<String, AvailabilityUnit> mapAvailability;
        AvailabilityUnit availability;
        Collection<Armor> armors;
        Collection<Weapon> weapons;
        Interval weaponsInterval;
        Collection<ArmyBuilderUnitConstraint> constraints;

        mapAvailability = new LinkedHashMap<>();
        for (final Unit unit : getUnitDAO().getUnits()) {
            armors = getArmors().get(unit.getUnitName());
            weapons = getWeapons().get(unit.getUnitName());
            weaponsInterval = getWeaponIntervals().get(unit.getUnitName());
            constraints = getConstraints().get(unit.getUnitName());

            availability = new DefaultAvailabilityUnit(unit, armors, weapons,
                    weaponsInterval.getLowerLimit(),
                    weaponsInterval.getUpperLimit(), constraints);

            availability.setArmor(getArmor().get(unit.getUnitName()));

            mapAvailability.put(unit.getUnitName(), availability);
        }

        return mapAvailability;
    }

    @Override
    public final void setUnitDAO(final UnitDAO dao) {
        daoUnit = dao;
    }

    protected final Map<String, Armor> getArmor() {
        return armor;
    }

    protected final Map<String, Collection<Armor>> getArmors() {
        return armors;
    }

    protected final Map<String, Collection<ArmyBuilderUnitConstraint>>
            getConstraints() {
        return constraints;
    }

    protected final UnitDAO getUnitDAO() {
        return daoUnit;
    }

    protected final Map<String, Interval> getWeaponIntervals() {
        return weaponIntervals;
    }

    protected final Map<String, Collection<Weapon>> getWeapons() {
        return weapons;
    }

}
