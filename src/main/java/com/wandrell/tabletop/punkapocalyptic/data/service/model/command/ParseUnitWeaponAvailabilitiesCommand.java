package com.wandrell.tabletop.punkapocalyptic.data.service.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseUnitWeaponAvailabilitiesCommand implements
        ReturnCommand<Collection<UnitWeaponAvailability>>, ModelServiceAware {

    private final Document                      doc;
    private final Repository<WeaponEnhancement> enhanceRepo;
    private final Map<String, Interval>         intervals;
    private ModelService                        modelService;
    private final Repository<Unit>              unitRepo;
    private final Repository<Weapon>            weaponRepo;

    public ParseUnitWeaponAvailabilitiesCommand(final Document doc,
            final Repository<Unit> unitRepo,
            final Repository<Weapon> weaponRepo,
            final Repository<WeaponEnhancement> enhanceRepo) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(unitRepo, "Received a null pointer as units repository");
        checkNotNull(weaponRepo,
                "Received a null pointer as weapons repository");
        checkNotNull(enhanceRepo,
                "Received a null pointer as weapon enhancements repository");

        this.doc = doc;
        this.unitRepo = unitRepo;
        this.weaponRepo = weaponRepo;
        this.enhanceRepo = enhanceRepo;

        intervals = getIntervalsMap();
    }

    @Override
    public final Collection<UnitWeaponAvailability> execute() throws Exception {
        final Collection<UnitWeaponAvailability> availabilities;
        UnitWeaponAvailability availability;

        availabilities = new LinkedList<>();

        for (final Unit unit : getUnitsRepository().getCollection(u -> true)) {
            availability = buildAvailability(unit);

            availabilities.add(availability);
        }

        return availabilities;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final UnitWeaponAvailability buildAvailability(final Unit unit) {
        final UnitWeaponAvailability availability;
        final Collection<WeaponOption> weaponOptions;
        final Interval weaponsInterval;
        final Integer minWeapons;
        final Integer maxWeapons;

        weaponOptions = getWeaponOptions(unit.getName());
        if (getIntervals().containsKey(unit.getName())) {
            weaponsInterval = getIntervals().get(unit.getName());
            minWeapons = weaponsInterval.getLowerLimit();
            maxWeapons = weaponsInterval.getUpperLimit();
        } else {
            minWeapons = 0;
            maxWeapons = 0;
        }

        availability = getModelService().getUnitWeaponAvailability(unit,
                weaponOptions, minWeapons, maxWeapons);

        return availability;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, Interval> getIntervals() {
        return intervals;
    }

    private final Map<String, Interval> getIntervalsMap() {
        final Map<String, Interval> intervals;
        final Collection<Element> nodes;
        Element intervalNode;
        Interval interval;
        Integer lower;
        Integer upper;

        nodes = XPathFactory.instance()
                .compile("//unit_weapon", Filters.element())
                .evaluate(getDocument());

        intervals = new LinkedHashMap<>();
        for (final Element node : nodes) {
            intervalNode = node.getChild("weapons_interval");

            lower = Integer.parseInt(intervalNode.getChild(
                    ModelNodeConf.MIN_WEAPONS).getValue());
            upper = Integer.parseInt(intervalNode.getChild(
                    ModelNodeConf.MAX_WEAPONS).getValue());

            interval = new DefaultInterval(lower, upper);

            intervals.put(node.getChildText(ModelNodeConf.UNIT), interval);
        }

        return intervals;
    }

    private final ModelService getModelService() {
        return modelService;
    }

    private final Repository<Unit> getUnitsRepository() {
        return unitRepo;
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
            enhancement = getWeaponsEnhancementsRepository()
                    .getCollection(
                            e -> e.getName().equals(node.getChildText("name")))
                    .iterator().next();
            enhancements.add(enhancement);
        }

        nodesWeapons = XPathFactory.instance()
                .compile(expWeapons, Filters.element()).evaluate(getDocument());

        weapons = new LinkedList<>();
        for (final Element node : nodesWeapons) {
            enhanWeapon = new LinkedList<>();
            weapon = getWeaponsRepository()
                    .getCollection(w -> w.getName().equals(node.getText()))
                    .iterator().next();

            for (final WeaponEnhancement enhanc : enhancements) {
                if (enhanc.isValid(weapon)) {
                    enhanWeapon.add(enhanc);
                }
            }

            weapons.add(getModelService().getWeaponOption(weapon, enhanWeapon));
        }

        return weapons;
    }

    private final Repository<WeaponEnhancement>
            getWeaponsEnhancementsRepository() {
        return enhanceRepo;
    }

    private final Repository<Weapon> getWeaponsRepository() {
        return weaponRepo;
    }

}
