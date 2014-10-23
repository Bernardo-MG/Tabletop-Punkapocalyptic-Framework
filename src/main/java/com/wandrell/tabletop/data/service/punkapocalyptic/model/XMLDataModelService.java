package com.wandrell.tabletop.data.service.punkapocalyptic.model;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.FactionUnitAvailability;
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
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseFactionUnitsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseFactionsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseInitialArmorsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseMeleeWeaponsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseRangedWeaponsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseRulesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitAvailabilitiesCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitGangConstraintsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseUnitsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponEnhancementsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponIntervalsCommand;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.command.ParseWeaponsRulesCommand;
import com.wandrell.util.command.CommandExecutor;
import com.wandrell.util.command.jdom.JDOMCombineFilesCommand;

public final class XMLDataModelService implements DataModelService {

    private final CommandExecutor                     executor;
    private Map<String, Faction>                      factions;
    private Map<String, Collection<AvailabilityUnit>> factionUnits;

    public XMLDataModelService(final CommandExecutor executor) {
        super();

        this.executor = executor;
    }

    @Override
    public final Collection<Faction> getAllFactions() {
        if (factions == null) {
            build();
        }

        return factions.values();
    }

    @Override
    public final Collection<AvailabilityUnit> getFactionUnits(
            final String faction) {
        if (factionUnits == null) {
            build();
        }

        return factionUnits.get(faction);
    }

    @Override
    public final Collection<UnitGangConstraint> getUnitConstraints(
            final String unit, final String faction) {
        final Faction fact;
        final Collection<FactionUnitAvailability> avas;
        final Collection<UnitGangConstraint> constraints;
        final Predicate<FactionUnitAvailability> predicate;

        fact = getFactions().get(faction);

        predicate = new Predicate<FactionUnitAvailability>() {

            @Override
            public final boolean apply(final FactionUnitAvailability input) {
                return input.getUnit().getUnitName().equals(unit);
            }

        };

        avas = Collections2.filter(fact.getUnits(), predicate);

        if (avas.isEmpty()) {
            constraints = null;
        } else {
            constraints = avas.iterator().next().getConstraints();
        }

        return constraints;
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
        final Map<String, AvailabilityUnit> unitAvailabilities;
        final Map<String, Armor> armorInitial;
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

        armorInitial = getExecutor().execute(
                new ParseInitialArmorsCommand(doc, armors));

        weaponIntervals = getExecutor().execute(
                new ParseWeaponIntervalsCommand(doc));

        unitAvailabilities = getExecutor().execute(
                new ParseUnitAvailabilitiesCommand(doc, units, armorInitial,
                        armors, weapons, weaponIntervals, equipment,
                        enhancements));

        getExecutor()
                .execute(new ParseWeaponsRulesCommand(doc, weapons, rules));

        factions = getExecutor().execute(new ParseFactionsCommand(doc));

        getExecutor().execute(
                new LoadFactionUnitsCommand(doc, factions.values(), units,
                        constraintsUnitGang));

        factionUnits = getExecutor().execute(
                new ParseFactionUnitsCommand(doc, unitAvailabilities));
    }

}
