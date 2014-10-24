package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.availability.DefaultUnitEquipmentAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitEquipmentAvailabilitiesCommand implements
        ReturnCommand<Map<String, UnitEquipmentAvailability>> {

    private final Document                       doc;
    private final Map<String, WeaponEnhancement> enhancements;
    private final Map<String, Equipment>         equipment;
    private final Map<String, Unit>              units;

    public ParseUnitEquipmentAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units,
            final Map<String, Equipment> equipment,
            final Map<String, WeaponEnhancement> enhancements) {
        super();

        this.doc = doc;
        this.units = units;
        this.equipment = equipment;
        this.enhancements = enhancements;
    }

    @Override
    public final Map<String, UnitEquipmentAvailability> execute()
            throws Exception {
        final Map<String, UnitEquipmentAvailability> availabilities;
        UnitEquipmentAvailability availability;

        availabilities = new LinkedHashMap<>();

        for (final Unit unit : getUnits().values()) {
            availability = buildAvailability(unit);

            availabilities.put(unit.getUnitName(), availability);
        }

        return availabilities;
    }

    private final UnitEquipmentAvailability buildAvailability(final Unit unit) {
        final UnitEquipmentAvailability availability;
        final Collection<WeaponEnhancement> weaponEnh;
        final Collection<Equipment> equipment;

        weaponEnh = getWeaponEnhancements(unit.getUnitName());
        equipment = getEquipment(unit.getUnitName());

        availability = new DefaultUnitEquipmentAvailability(weaponEnh,
                equipment);

        return availability;
    }

    private final Document getDocument() {
        return doc;
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
        return enhancements;
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

}
