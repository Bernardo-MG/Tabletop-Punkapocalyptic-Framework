package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

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

public final class ParseUnitAvailabilitiesCommand implements
        ReturnCommand<Map<String, AvailabilityUnit>> {

    private final Map<String, Armor>             armor;
    private final Map<String, Armor>             armors;
    private final Map<String, GangConstraint>    constraints;
    private final Document                       document;
    private final Map<String, Equipment>         equipment;
    private final Map<String, Unit>              units;
    private final Map<String, WeaponEnhancement> weaponEnhancements;
    private final Map<String, Interval>          weaponIntervals;
    private final Map<String, Weapon>            weapons;

    public ParseUnitAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units, final Map<String, Armor> armor,
            final Map<String, Armor> armors, final Map<String, Weapon> weapons,
            final Map<String, Interval> weaponIntervals,
            final Map<String, GangConstraint> constraints,
            final Map<String, Equipment> equipment,
            final Map<String, WeaponEnhancement> weaponEnhancements) {
        super();

        document = doc;
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
    public final Map<String, AvailabilityUnit> execute() throws Exception {
        final Map<String, AvailabilityUnit> units;
        AvailabilityUnit availability;

        units = new LinkedHashMap<>();

        for (final Unit unit : getUnits().values()) {
            availability = buildAvailability(unit);

            units.put(availability.getUnitName(), availability);
        }

        return units;
    }

    private final AvailabilityUnit buildAvailability(final Unit unit) {
        final AvailabilityUnit availability;
        final Collection<Armor> armors;
        final Collection<Weapon> weapons;
        final Interval weaponsInterval;
        final Collection<GangConstraint> constraints;
        final Collection<WeaponEnhancement> weaponEnhancements;
        final Collection<Equipment> equipment;

        armors = getArmors(unit.getUnitName());
        weapons = getWeapons(unit.getUnitName());
        weaponsInterval = getWeaponIntervals().get(unit.getUnitName());
        constraints = getConstraints(unit.getUnitName());
        weaponEnhancements = getWeaponEnhancements(unit.getUnitName());
        equipment = getEquipment(unit.getUnitName());

        availability = new AvailabilityUnitWrapper(unit, armors, weapons,
                weaponsInterval.getLowerLimit(),
                weaponsInterval.getUpperLimit(), constraints,
                weaponEnhancements, equipment);

        availability.setArmor(getArmor().get(unit.getUnitName()));

        return availability;
    }

    private final Map<String, Armor> getArmor() {
        return armor;
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

    private final Collection<Armor> getArmors(final String unit) {
        final Collection<Armor> armors;
        final Collection<Element> nodes;
        final String expression;
        Armor armor;

        armors = new LinkedList<>();

        expression = String.format(
                "//unit_armors/unit_armor[unit='%s']/armors/armor", unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        for (final Element node : nodes) {
            armor = getArmors().get(node.getChildText("name"));
            armor.setCost(Integer.parseInt(node.getChildText("cost")));
            armors.add(armor);
        }

        return armors;
    }

    private final Map<String, GangConstraint> getConstraints() {
        return constraints;
    }

    private final Collection<GangConstraint> getConstraints(final String unit) {
        final Collection<GangConstraint> constraints;
        final Collection<Element> nodes;
        final String expression;

        expression = String.format(
                "//unit_availability/units/unit[name='%s']/constraints", unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        constraints = new LinkedList<>();
        for (final Element node : nodes) {
            constraints.add(getConstraints().get(node.getText()));
        }

        return constraints;
    }

    private final Document getDocument() {
        return document;
    }

    private final Map<String, Equipment> getEquipment() {
        return equipment;
    }

    private final Collection<Equipment> getEquipment(final String unit) {
        final Collection<Equipment> equipment;
        final Collection<Element> nodes;
        final String expression;

        expression = String
                .format("//unit_equipment_pieces/unit_equipment_piece[unit='%s']/equipment_pieces/equipment_piece",
                        unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        equipment = new LinkedList<>();
        for (final Element node : nodes) {
            equipment.add(getEquipment().get(node.getText()));
        }

        return equipment;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

    private final Map<String, WeaponEnhancement> getWeaponEnhancements() {
        return weaponEnhancements;
    }

    private final Collection<WeaponEnhancement> getWeaponEnhancements(
            final String unit) {
        final Collection<WeaponEnhancement> enhancements;
        final Collection<Element> nodes;
        final String expression;

        expression = String
                .format("//unit_weapon_enhancements/unit_weapon_enhancement[unit='%s']/weapon_enhancements/weapon_enhancement",
                        unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        // TODO
        enhancements = new LinkedList<>();
        for (final Element node : nodes) {
            enhancements.add(getWeaponEnhancements().get(node.getText()));
        }

        return enhancements;
    }

    private final Map<String, Interval> getWeaponIntervals() {
        return weaponIntervals;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

    private final Collection<Weapon> getWeapons(final String unit) {
        final Collection<Weapon> weapons;
        final Collection<Element> nodes;
        final String expression;

        expression = String.format(
                "//unit_weapons/unit_weapon[unit='%s']/weapons/weapon", unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        weapons = new LinkedList<>();
        for (final Element node : nodes) {
            weapons.add(getWeapons().get(node.getText()));
        }

        return weapons;
    }

}
