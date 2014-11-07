package com.wandrell.tabletop.data.service.punkapocalyptic.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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

    private Map<String, UnitArmorAvailability>  avaArmor;
    private Map<String, UnitWeaponAvailability> avaWeapon;
    private final CommandExecutor               executor;
    private Map<String, Faction>                factions;
    private final Collection<InputStream>       sources;

    public XMLDataModelService(final CommandExecutor executor,
            final Collection<InputStream> sources) {
        super();

        checkNotNull(executor, "Received a null pointer as executor");
        checkNotNull(sources, "Received a null pointer as sources");

        checkArgument((sources.size() % 2) == 0,
                "The sources should be an even number");

        this.executor = executor;
        this.sources = sources;
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

        checkNotNull(faction, "Received a null pointer as faction name");

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
        checkNotNull(unit, "Received a null pointer as unit name");

        return getArmorAvailabilities().get(unit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Collection<UnitGangConstraint> getUnitConstraints(
            final String unit, final String faction) {
        final JXPathContext context;
        final Faction fact;
        final String query;

        checkNotNull(unit, "Received a null pointer as unit name");
        checkNotNull(faction, "Received a null pointer as faction name");

        fact = getFactions().get(faction);
        context = JXPathContext.newContext(fact);
        query = "units[unit/unitName=$unit]/constraints";

        context.getVariables().declareVariable("unit", unit);

        return (Collection<UnitGangConstraint>) context.getValue(query);
    }

    @Override
    public final UnitWeaponAvailability getUnitWeaponAvailability(
            final String unit) {
        checkNotNull(unit, "Received a null pointer as unit name");

        return getWeaponAvailabilities().get(unit);
    }

    private final Map<String, UnitArmorAvailability> getArmorAvailabilities() {
        if (avaArmor == null) {
            initialize();
        }

        return avaArmor;
    }

    private final CommandExecutor getExecutor() {
        return executor;
    }

    private final Map<String, Faction> getFactions() {
        if (factions == null) {
            initialize();
        }

        return factions;
    }

    private final Collection<InputStream> getSources() {
        return sources;
    }

    private final Map<String, UnitWeaponAvailability> getWeaponAvailabilities() {
        if (avaWeapon == null) {
            initialize();
        }

        return avaWeapon;
    }

    private final void initialize() {
        parseModel(getExecutor().execute(
                new JDOMCombineFilesCommand(getSources())));
    }

    private final void parseModel(final Document doc) {
        final Map<String, UnitGangConstraint> constUnitGang;
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
        @SuppressWarnings("unused")
        final Map<String, UnitEquipmentAvailability> avaEquipment;

        constUnitGang = getExecutor().execute(
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

        avaWeapon = getExecutor().execute(
                new ParseUnitWeaponAvailabilitiesCommand(doc, units, weapons,
                        enhancements, weaponIntervals));

        avaArmor = getExecutor().execute(
                new ParseUnitArmorAvailabilitiesCommand(doc, units, armors));

        avaEquipment = getExecutor().execute(
                new ParseUnitEquipmentAvailabilitiesCommand(doc, units,
                        equipment, enhancements));

        getExecutor()
                .execute(new ParseWeaponsRulesCommand(doc, weapons, rules));

        factions = getExecutor().execute(new ParseFactionsCommand(doc));

        getExecutor().execute(
                new LoadFactionUnitsCommand(doc, factions.values(), units,
                        constUnitGang));
    }

}
