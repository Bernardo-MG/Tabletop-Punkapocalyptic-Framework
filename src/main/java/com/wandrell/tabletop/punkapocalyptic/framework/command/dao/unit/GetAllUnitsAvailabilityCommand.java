package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.UnitDAO;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.UnitDAOAware;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.punkapocalyptic.unit.DefaultAvailabilityUnit;
import com.wandrell.tabletop.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllUnitsAvailabilityCommand implements
        ReturnCommand<Map<String, AvailabilityUnit>>, UnitDAOAware {

    private final Map<String, Armor>              armor;
    private final Map<String, Collection<Armor>>  armors;
    private UnitDAO                               daoUnit;
    private final Map<String, Interval>           weaponIntervals;
    private final Map<String, Collection<Weapon>> weapons;

    public GetAllUnitsAvailabilityCommand(final Map<String, Armor> armor,
            final Map<String, Collection<Armor>> armors,
            final Map<String, Collection<Weapon>> weapons,
            final Map<String, Interval> weaponIntervals) {
        super();

        this.armor = armor;
        this.armors = armors;
        this.weapons = weapons;
        this.weaponIntervals = weaponIntervals;
    }

    @Override
    public final Map<String, AvailabilityUnit> execute() {
        final Map<String, AvailabilityUnit> mapAvailability;
        AvailabilityUnit availability;
        Collection<Armor> armors;
        Collection<Weapon> weapons;
        Interval weaponsInterval;

        mapAvailability = new LinkedHashMap<>();
        for (final Unit unit : getUnitDAO().getUnits()) {
            armors = getArmors().get(unit.getUnitName());
            weapons = getWeapons().get(unit.getUnitName());
            weaponsInterval = getWeaponIntervals().get(unit.getUnitName());

            availability = new DefaultAvailabilityUnit(unit, armors, weapons,
                    weaponsInterval.getLowerLimit(),
                    weaponsInterval.getUpperLimit());

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
