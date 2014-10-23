package com.wandrell.tabletop.data.service.punkapocalyptic.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.jdom2.Document;

import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitArmorAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.LoadFactionUnitsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ModelInputStreamsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseArmorInitializersCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseArmorsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseEquipmentCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseFactionsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseMeleeWeaponsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseRangedWeaponsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseRulesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitArmorAvailabilitiesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitEquipmentAvailabilitiesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitGangConstraintsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitWeaponAvailabilitiesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponEnhancementsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponIntervalsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponsRulesCommand;
import com.wandrell.util.command.CommandExecutor;
import com.wandrell.util.command.jdom.JDOMCombineFilesCommand;

public final class XMLDataModelService implements DataModelService {

    private Map<String, UnitArmorAvailability>     availabilitiesArmor;
    private Map<String, UnitEquipmentAvailability> availabilitiesEquipment;
    private Map<String, UnitWeaponAvailability>    availabilitiesWeapon;
    private final CommandExecutor                  executor;
    private Map<String, Faction>                   factions;

    public XMLDataModelService(final CommandExecutor executor) {
        super();

        this.executor = executor;
    }

    @Override
    public final Collection<Faction> getAllFactions() {
        return getFactions().values();
    }

    @Override
    public final Collection<Unit> getFactionUnits(final String faction) {
        final JXPathContext context;
        final Faction fact;
        final String query;
        final Collection<Unit> result;
        Object obj;

        fact = getFactions().get(faction);
        context = JXPathContext.newContext(fact);
        query = "units/unit";

        result = new LinkedList<>();
        for (final Iterator<?> itr = context.iterate(query); itr.hasNext();) {
            obj = itr.next();
            result.add((Unit) obj);
        }

        return result;
    }

    @Override
    public final UnitArmorAvailability getUnitArmorAvailability(
            final String unit) {
        if (availabilitiesArmor == null) {
            build();
        }

        return availabilitiesArmor.get(unit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Collection<UnitGangConstraint> getUnitConstraints(
            final String unit, final String faction) {
        final JXPathContext context;
        final Faction fact;
        final String query;

        fact = getFactions().get(faction);
        context = JXPathContext.newContext(fact);
        query = "units[unit/unitName=$unit]/constraints";

        context.getVariables().declareVariable("unit", unit);

        return (Collection<UnitGangConstraint>) context.getValue(query);
    }

    @Override
    public final UnitEquipmentAvailability getUnitEquipmentAvailability(
            final String unit) {
        if (availabilitiesEquipment == null) {
            build();
        }

        return availabilitiesEquipment.get(unit);
    }

    @Override
    public final UnitWeaponAvailability getUnitWeaponAvailability(
            final String unit) {
        if (availabilitiesWeapon == null) {
            build();
        }

        return availabilitiesWeapon.get(unit);
    }

    private final void build() {
        parseModel(getDocument());
    }

    private final Document getDocument() {
        final Map<InputStream, InputStream> sources;

        sources = getExecutor().execute(new ModelInputStreamsCommand());

        return getExecutor().execute(new JDOMCombineFilesCommand(sources));
    }

    private final CommandExecutor getExecutor() {
        return executor;
    }

    private final Map<String, Faction> getFactions() {
        if (factions == null) {
            build();
        }

        return factions;
    }

    private final void parseModel(final Document doc) {
        final Map<String, UnitGangConstraint> constraintsUnitGang;
        final Map<String, ArmorInitializerModifier> initializersArmor;
        final Map<String, MeleeWeapon> weaponsMelee;
        final Map<String, RangedWeapon> weaponsRanged;
        final Map<String, Weapon> weapons;
        final Map<String, SpecialRule> rules;
        final Map<String, Armor> armors;
        final Map<String, Unit> units;
        final Map<String, Equipment> equipment;
        final Map<String, WeaponEnhancement> enhancements;
        final Map<String, Interval> weaponIntervals;

        constraintsUnitGang = getExecutor().execute(
                new ParseUnitGangConstraintsCommand(doc));
        initializersArmor = getExecutor().execute(
                new ParseArmorInitializersCommand(doc));

        weaponsMelee = getExecutor().execute(new ParseMeleeWeaponsCommand(doc));
        weaponsRanged = getExecutor().execute(
                new ParseRangedWeaponsCommand(doc, weaponsMelee
                        .get(WeaponNameConf.LIGHT_MACE)));

        weapons = new LinkedHashMap<>();
        weapons.putAll(weaponsMelee);
        weapons.putAll(weaponsRanged);

        rules = getExecutor().execute(
                new ParseRulesCommand(doc, weaponsMelee
                        .get(WeaponNameConf.IMPROVISED_WEAPON)));

        armors = getExecutor().execute(
                new ParseArmorsCommand(doc, rules, initializersArmor));

        units = getExecutor().execute(new ParseUnitsCommand(doc));

        equipment = getExecutor().execute(new ParseEquipmentCommand(doc));

        enhancements = getExecutor().execute(
                new ParseWeaponEnhancementsCommand(doc));

        weaponIntervals = getExecutor().execute(
                new ParseWeaponIntervalsCommand(doc));

        availabilitiesWeapon = getExecutor().execute(
                new ParseUnitWeaponAvailabilitiesCommand(doc, units, weapons,
                        weaponIntervals));

        availabilitiesArmor = getExecutor().execute(
                new ParseUnitArmorAvailabilitiesCommand(doc, units, armors));

        availabilitiesEquipment = getExecutor().execute(
                new ParseUnitEquipmentAvailabilitiesCommand(doc, units,
                        equipment, enhancements));

        getExecutor()
                .execute(new ParseWeaponsRulesCommand(doc, weapons, rules));

        factions = getExecutor().execute(new ParseFactionsCommand(doc));

        getExecutor().execute(
                new LoadFactionUnitsCommand(doc, factions.values(), units,
                        constraintsUnitGang));
    }

}
