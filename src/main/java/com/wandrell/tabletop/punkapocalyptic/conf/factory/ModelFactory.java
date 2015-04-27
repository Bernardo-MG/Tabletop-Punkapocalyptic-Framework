package com.wandrell.tabletop.punkapocalyptic.conf.factory;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.procedure.ConstraintData;
import com.wandrell.tabletop.punkapocalyptic.conf.ConstraintsConf;
import com.wandrell.tabletop.punkapocalyptic.conf.MessageBundleKey;
import com.wandrell.tabletop.punkapocalyptic.conf.SpecialRuleNameConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultFactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.DefaultWeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.faction.DefaultFaction;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultArmor;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultEquipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultRangedArmor;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultRangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.FirearmWeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.RangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitBasedStrengthRangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.DefaultSpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.TwoHandedSpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DerivedValuesBuilder;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnitWrapper;
import com.wandrell.tabletop.punkapocalyptic.model.unit.RulesetServiceDerivedValuesBuilder;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.UnitTemplate;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.DefaultMutation;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.model.util.DefaultRangedValue;
import com.wandrell.tabletop.punkapocalyptic.model.util.RangedValue;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.GangValorationValueBox;
import com.wandrell.tabletop.valuebox.DefaultValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class ModelFactory {

    private static final ModelFactory INSTANCE = new ModelFactory();

    public static final ModelFactory getInstance() {
        return INSTANCE;
    }

    private ModelFactory() {
        super();
        // TODO: Remove and do this on the commands or the service
    }

    public final Armor getArmor(final String name, final Integer armorValue,
            final Collection<SpecialRule> rules) {
        final Armor armor;
        final RangedValue rangedArmor;

        switch (name) {
            case "bulletproof_vest":
                rangedArmor = new DefaultRangedValue(5, 6, 7);
                armor = new DefaultRangedArmor(name, armorValue, rangedArmor,
                        rules);
                break;
            default:
                armor = new DefaultArmor(name, armorValue, rules);
        }

        return armor;
    }

    public final Constraint getConstraint(final Gang gang, final String name,
            final String unit, final List<String> context,
            final LocalizationService service) {
        final Constraint constraint;
        final String message;
        // TODO: The localization service should not be required in here

        switch (name) {
            case ConstraintsConf.UNIQUE:
                message = String
                        .format(service
                                .getMessageString(MessageBundleKey.UNIQUE),
                                unit);

                constraint = new UnitUpToACountConstraint(gang, unit, 1,
                        message);
                break;
            case ConstraintsConf.UP_TO_HALF_POINTS:
                message = String.format(
                        service.getMessageString(MessageBundleKey.HALF_GANG),
                        unit);

                constraint = new UnitUpToHalfGangLimitConstraint(gang, unit,
                        message);
                break;
            case ConstraintsConf.DEPENDANT:
                message = String.format(service
                        .getMessageString(MessageBundleKey.DEPENDS_ON_UNIT),
                        unit, context.get(0), Integer.parseInt(context.get(1)));

                constraint = new DependantUnitConstraint(gang, context.get(0),
                        unit, Integer.parseInt(context.get(1)), message);
                break;
            default:
                constraint = null;
        }

        return constraint;
    }

    public final Equipment getEquipment(final String name, final Integer cost) {
        return new DefaultEquipment(name, cost);
    }

    public final Faction getFaction(final String name) {
        return new DefaultFaction(name);
    }

    public final FactionUnitAvailability getFactionUnitAvailability(
            final Faction faction, final UnitTemplate unit,
            final Collection<ConstraintData> constraints) {
        return new DefaultFactionUnitAvailability(faction, unit, constraints);
    }

    public final Gang getGang(final Faction faction,
            final RulesetService service) {
        final DefaultGang.ValorationBuilder valorationBuilder;
        final Gang gang;

        valorationBuilder = new DefaultGang.ValorationBuilder() {

            @Override
            public final ValueBox getValoration(final Gang gang) {
                return new GangValorationValueBox(gang, service);
            }

        };
        gang = new DefaultGang(faction, valorationBuilder);

        return gang;
    }

    public final MeleeWeapon getMeleeWeapon(final String name,
            final Integer cost, final Integer strength,
            final Integer penetration, final Integer combat,
            final Collection<SpecialRule> rules) {
        return new DefaultMeleeWeapon(name, cost, strength, penetration,
                combat, rules);
    }

    public final Mutation getMutation(final String name, final Integer cost,
            final Integer actions, final Integer agility, final Integer combat,
            final Integer precision, final Integer strength,
            final Integer tech, final Integer toughness) {
        return new DefaultMutation(name, cost, actions, agility, combat,
                precision, strength, tech, toughness);
    }

    public final RangedValue getRangedValue(final Integer distanceShort,
            final Integer distanceMedium, final Integer distanceLong) {
        return new DefaultRangedValue(distanceShort, distanceMedium,
                distanceLong);
    }

    public final RangedWeapon getRangedWeapon(final Unit unit,
            final String name, final Integer cost,
            final Collection<SpecialRule> rules, final RangedValue penetration,
            final RangedValue strength, final RangedValue distanceCM,
            final RangedValue distanceInches, final Boolean firearm) {
        final RangedWeapon weapon;

        switch (name) {
            case "throwing_knife":
                weapon = new UnitBasedStrengthRangedWeapon(unit, name, cost,
                        rules, penetration, strength, distanceCM,
                        distanceInches, firearm);
                break;
            default:
                weapon = new DefaultRangedWeapon(name, cost, rules,
                        penetration, strength, distanceCM, distanceInches,
                        firearm);
        }

        return weapon;
    }

    public final SpecialRule getSpecialRule(final String name,
            final RulesetService service) {
        final SpecialRule rule;

        switch (name) {
            case SpecialRuleNameConf.TWO_HANDED:
                rule = new TwoHandedSpecialRule(SpecialRuleNameConf.TWO_HANDED);
                break;
            default:
                rule = new DefaultSpecialRule(name);
        }

        return rule;
    }

    public final Unit getUnit(final UnitTemplate template,
            final RulesetService service) {
        final DerivedValuesBuilder valorationBuilder;
        final Collection<SpecialRule> filtered;
        Unit unit;

        valorationBuilder = new RulesetServiceDerivedValuesBuilder(service);

        unit = new DefaultUnit(template, valorationBuilder);

        filtered = Collections2.filter(
                unit.getUnitTemplate().getSpecialRules(),
                new Predicate<SpecialRule>() {

                    @Override
                    public final boolean apply(final SpecialRule input) {
                        return input.getName().equals("pack");
                    }

                });

        if (!filtered.isEmpty()) {
            unit = new GroupedUnitWrapper(unit, new DefaultValueBox(0));
        }

        return unit;
    }

    public final UnitArmorAvailability getUnitArmorAvailability(
            final UnitTemplate unit,
            final Collection<ArmorOption> armorOptions,
            final ArmorOption initialArmor) {
        return new DefaultUnitArmorAvailability(unit, armorOptions,
                initialArmor);
    }

    public final UnitMutationAvailability getUnitMutationAvailability(
            final UnitTemplate unit, final Integer max,
            final Collection<Mutation> mutations) {
        return new DefaultUnitMutationAvailability(unit, max, mutations);
    }

    public final UnitWeaponAvailability getUnitWeaponAvailability(
            final UnitTemplate unit,
            final Collection<WeaponOption> weaponOptions,
            final Integer minWeapons, final Integer maxWeapons) {
        return new DefaultUnitWeaponAvailability(unit, weaponOptions,
                minWeapons, maxWeapons);
    }

    public final WeaponEnhancement getWeaponEnhancement(final String name,
            final Integer cost) {
        final WeaponEnhancement enhancement;

        if ("bayonet".equals(name)) {
            enhancement = new FirearmWeaponEnhancement("bayonet", cost);
        } else {
            enhancement = null;
        }

        return enhancement;
    }

    public final WeaponOption getWeaponOption(final Weapon weapon,
            final Collection<WeaponEnhancement> enhancements) {
        return new DefaultWeaponOption(weapon, enhancements);
    }

}
