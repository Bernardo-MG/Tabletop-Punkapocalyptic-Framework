package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.DefaultUnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitWeaponAvailabilitiesCommand implements
        ReturnCommand<Map<String, UnitWeaponAvailability>> {

    private final Document              doc;
    private final Map<String, Unit>     units;
    private final Map<String, Interval> weaponIntervals;
    private final Map<String, Weapon>   weapons;

    public ParseUnitWeaponAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units, final Map<String, Weapon> weapons,
            final Map<String, Interval> weaponIntervals) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(units, "Received a null pointer as units");
        checkNotNull(weapons, "Received a null pointer as weapons");
        checkNotNull(weaponIntervals,
                "Received a null pointer as weapon intervals");

        this.doc = doc;
        this.units = units;
        this.weapons = weapons;
        this.weaponIntervals = weaponIntervals;
    }

    private final UnitWeaponAvailability buildAvailability(final Unit unit) {
        final UnitWeaponAvailability availability;
        final Collection<Weapon> weaponOptions;
        final Integer minWeapons;
        final Integer maxWeapons;

        weaponOptions = getWeapons(unit.getUnitName());
        minWeapons = getWeaponIntervals().get(unit.getUnitName())
                .getLowerLimit();
        maxWeapons = getWeaponIntervals().get(unit.getUnitName())
                .getUpperLimit();

        availability = new DefaultUnitWeaponAvailability(weaponOptions,
                minWeapons, maxWeapons);

        return availability;
    }

    @Override
    public final Map<String, UnitWeaponAvailability> execute() throws Exception {
        final Map<String, UnitWeaponAvailability> availabilities;
        UnitWeaponAvailability availability;

        availabilities = new LinkedHashMap<>();

        for (final Unit unit : getUnits().values()) {
            availability = buildAvailability(unit);

            availabilities.put(unit.getUnitName(), availability);
        }

        return availabilities;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, Unit> getUnits() {
        return units;
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
