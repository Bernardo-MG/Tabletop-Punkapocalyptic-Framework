package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnitWrapper;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllUnitsAvailabilityCommand implements
        ReturnCommand<Map<String, AvailabilityUnit>> {

    private final Map<String, Armor>                         armor;
    private final Map<String, Collection<Armor>>             armors;
    private final Map<String, Collection<GangConstraint>>    constraints;
    private final Map<String, Collection<Equipment>>         equipment;
    private final Map<String, Unit>                          units;
    private final Map<String, Collection<WeaponEnhancement>> weaponEnhancements;
    private final Map<String, Interval>                      weaponIntervals;
    private final Map<String, Collection<Weapon>>            weapons;

    public GetAllUnitsAvailabilityCommand(final Map<String, Unit> units,
            final Map<String, Armor> armor,
            final Map<String, Collection<Armor>> armors,
            final Map<String, Collection<Weapon>> weapons,
            final Map<String, Interval> weaponIntervals,
            final Map<String, Collection<GangConstraint>> constraints,
            final Map<String, Collection<Equipment>> equipment,
            final Map<String, Collection<WeaponEnhancement>> weaponEnhancements) {
        super();

        checkNotNull(units, "Received a null pointer as units");
        checkNotNull(armor, "Received a null pointer as armor");
        checkNotNull(armors, "Received a null pointer as armors");
        checkNotNull(weaponIntervals,
                "Received a null pointer as weapon intervals");
        checkNotNull(constraints, "Received a null pointer as constraints");

        this.units = units;
        this.armor = armor;
        this.armors = armors;
        this.weapons = weapons;
        this.weaponIntervals = weaponIntervals;
        this.constraints = constraints;
        this.equipment = equipment;
        this.weaponEnhancements = weaponEnhancements;
    }

    @Override
    public final Map<String, AvailabilityUnit> execute() {
        final Map<String, AvailabilityUnit> mapAvailability;
        AvailabilityUnit availability;
        Collection<Armor> armors;
        Collection<Weapon> weapons;
        Interval weaponsInterval;
        Collection<GangConstraint> constraints;
        Collection<WeaponEnhancement> weaponEnhancements;
        Collection<Equipment> equipment;

        mapAvailability = new LinkedHashMap<>();
        for (final Unit unit : getUnits().values()) {
            armors = getArmors().get(unit.getUnitName());
            weapons = getWeapons().get(unit.getUnitName());
            weaponsInterval = getWeaponIntervals().get(unit.getUnitName());
            constraints = getConstraints().get(unit.getUnitName());
            weaponEnhancements = getWeaponEnhancements()
                    .get(unit.getUnitName());
            equipment = getEquipment().get(unit.getUnitName());

            availability = new AvailabilityUnitWrapper(unit, armors, weapons,
                    weaponsInterval.getLowerLimit(),
                    weaponsInterval.getUpperLimit(), constraints,
                    weaponEnhancements, equipment);

            availability.setArmor(getArmor().get(unit.getUnitName()));

            mapAvailability.put(unit.getUnitName(), availability);
        }

        return mapAvailability;
    }

    private final Map<String, Armor> getArmor() {
        return armor;
    }

    private final Map<String, Collection<Armor>> getArmors() {
        return armors;
    }

    private final Map<String, Collection<GangConstraint>> getConstraints() {
        return constraints;
    }

    private final Map<String, Collection<Equipment>> getEquipment() {
        return equipment;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

    private final Map<String, Collection<WeaponEnhancement>>
            getWeaponEnhancements() {
        return weaponEnhancements;
    }

    private final Map<String, Interval> getWeaponIntervals() {
        return weaponIntervals;
    }

    private final Map<String, Collection<Weapon>> getWeapons() {
        return weapons;
    }

}
