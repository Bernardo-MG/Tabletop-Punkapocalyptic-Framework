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
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.DefaultWeaponOption;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.WeaponOption;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitWeaponAvailabilitiesCommand implements
        ReturnCommand<Map<String, UnitWeaponAvailability>> {

    private final Document                       doc;
    private final Map<String, WeaponEnhancement> enhancements;
    private final Map<String, Unit>              units;
    private final Map<String, Interval>          weaponIntervals;
    private final Map<String, Weapon>            weapons;

    public ParseUnitWeaponAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units, final Map<String, Weapon> weapons,
            final Map<String, WeaponEnhancement> enhancements,
            final Map<String, Interval> weaponIntervals) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(units, "Received a null pointer as units");
        checkNotNull(weapons, "Received a null pointer as weapons");
        checkNotNull(enhancements,
                "Received a null pointer as weapon enhancements");
        checkNotNull(weaponIntervals,
                "Received a null pointer as weapon intervals");

        this.doc = doc;
        this.units = units;
        this.weapons = weapons;
        this.enhancements = enhancements;
        this.weaponIntervals = weaponIntervals;
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

    private final UnitWeaponAvailability buildAvailability(final Unit unit) {
        final UnitWeaponAvailability availability;
        final Collection<WeaponOption> weaponOptions;
        final Integer minWeapons;
        final Integer maxWeapons;

        weaponOptions = getWeaponOptions(unit.getUnitName());
        minWeapons = getWeaponIntervals().get(unit.getUnitName())
                .getLowerLimit();
        maxWeapons = getWeaponIntervals().get(unit.getUnitName())
                .getUpperLimit();

        availability = new DefaultUnitWeaponAvailability(weaponOptions,
                minWeapons, maxWeapons);

        return availability;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

    private final Map<String, WeaponEnhancement> getWeaponEnhancements() {
        return enhancements;
    }

    private final Map<String, Interval> getWeaponIntervals() {
        return weaponIntervals;
    }

    private final Collection<WeaponOption> getWeaponOptions(final String unit) {
        final Collection<WeaponEnhancement> enhancements;
        final Collection<WeaponOption> weapons;
        final Collection<Element> nodesWeapons;
        final Collection<Element> nodesFactions;
        final Collection<Element> nodesEnhancements;
        final String expWeapons;
        final String expFaction;
        final String expEnhancements;
        final String faction;
        Collection<WeaponEnhancement> enhanWeapon;
        WeaponEnhancement enhancement;
        Weapon weapon;

        expWeapons = String.format(
                "//unit_weapons/unit_weapon[unit='%s']/weapons/weapon", unit);
        expFaction = String.format(
                "//faction_unit//unit[name='%s']/../../faction", unit);

        nodesFactions = XPathFactory.instance()
                .compile(expFaction, Filters.element()).evaluate(getDocument());
        faction = nodesFactions.iterator().next().getText();

        expEnhancements = String
                .format("//faction_weapon_enhancement[faction='%s']//weapon_enhancement",
                        faction);

        nodesEnhancements = XPathFactory.instance()
                .compile(expEnhancements, Filters.element())
                .evaluate(getDocument());

        enhancements = new LinkedList<>();
        for (final Element node : nodesEnhancements) {
            enhancement = getWeaponEnhancements().get(
                    node.getChild("name").getText());
            enhancements.add(enhancement);
        }

        nodesWeapons = XPathFactory.instance()
                .compile(expWeapons, Filters.element()).evaluate(getDocument());

        weapons = new LinkedList<>();
        for (final Element node : nodesWeapons) {
            enhanWeapon = new LinkedList<>();
            weapon = getWeapons().get(node.getText());

            for (final WeaponEnhancement enhanc : enhancements) {
                if (enhanc.isValid(weapon)) {
                    enhanWeapon.add(enhanc);
                }
            }

            weapons.add(new DefaultWeaponOption(weapon, enhanWeapon));
        }

        return weapons;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
