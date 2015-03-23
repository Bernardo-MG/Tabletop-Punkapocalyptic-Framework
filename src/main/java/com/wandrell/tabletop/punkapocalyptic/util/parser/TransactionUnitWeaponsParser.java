package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.conf.ConstraintsConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultWeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.WeaponOption;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;

public final class TransactionUnitWeaponsParser implements
        Parser<Map<String, Object>, UnitWeaponAvailability> {

    private final Repository<WeaponEnhancement> enhancementsRepo;
    private final Repository<Unit>              unitsRepo;
    private final Repository<Weapon>            weaponsRepo;

    public TransactionUnitWeaponsParser(final Repository<Unit> unitsRepo,
            final Repository<Weapon> weaponsRepo,
            final Repository<WeaponEnhancement> enhancementsRepo) {
        super();

        this.unitsRepo = unitsRepo;
        this.weaponsRepo = weaponsRepo;
        this.enhancementsRepo = enhancementsRepo;
    }

    public final Constraint getConstraint(final String name, final String unit,
            final Collection<String> tags) {
        final Constraint constraint;
        final String message;
        final Iterator<String> tagsItr;

        switch (name) {
            case ConstraintsConf.UNIQUE:
                message = "";

                constraint = new UnitUpToACountConstraint(unit, 1, message);
                break;
            case ConstraintsConf.UP_TO_HALF_POINTS:
                message = "";

                constraint = new UnitUpToHalfGangLimitConstraint(unit, message);
                break;
            case ConstraintsConf.DEPENDANT:
                message = "";
                tagsItr = tags.iterator();

                constraint = new DependantUnitConstraint(tagsItr.next(), unit,
                        Integer.parseInt(tagsItr.next()), message);
                break;
            default:
                constraint = null;
        }

        return constraint;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final UnitWeaponAvailability parse(final Map<String, Object> input) {
        final Unit unit;
        final Collection<Weapon> weapons;
        final Collection<WeaponEnhancement> enhancements;
        final Collection<String> weaponNames;
        final Collection<String> enhancementNames;
        final Collection<WeaponOption> options;
        final Integer min;
        final Integer max;
        Collection<WeaponEnhancement> validEnhancements;

        unit = unitsRepo.getCollection(new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit unit) {
                return unit.getName().equals(input.get("unit"));
            }

        }).iterator().next();

        weaponNames = (Collection<String>) input.get("weapons");
        weapons = weaponsRepo.getCollection(new Predicate<Weapon>() {

            @Override
            public final boolean apply(final Weapon weapon) {
                return weaponNames.contains(weapon.getName());
            }

        });

        enhancementNames = (Collection<String>) input.get("enhancements");
        enhancements = enhancementsRepo
                .getCollection(new Predicate<WeaponEnhancement>() {

                    @Override
                    public final boolean apply(
                            final WeaponEnhancement enhancement) {
                        return enhancementNames.contains(enhancement.getName());
                    }

                });

        options = new LinkedList<>();
        for (final Weapon weapon : weapons) {
            validEnhancements = new LinkedList<>();
            for (final WeaponEnhancement enhancement : enhancements) {
                if (enhancement.isValid(weapon)) {
                    validEnhancements.add(enhancement);
                }
            }
            options.add(new DefaultWeaponOption(weapon, validEnhancements));
        }

        min = (Integer) input.get("min_weapons");
        max = (Integer) input.get("max_weapons");

        // TODO: Use a service
        return new DefaultUnitWeaponAvailability(unit, options, min, max);
    }

}
