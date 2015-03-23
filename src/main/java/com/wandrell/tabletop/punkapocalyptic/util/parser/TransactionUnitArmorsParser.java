package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.conf.ConstraintsConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitArmorAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;

public final class TransactionUnitArmorsParser implements
        Parser<Map<String, Object>, UnitArmorAvailability> {

    private final Repository<Armor> armorsRepo;
    private final Repository<Unit>  unitsRepo;

    public TransactionUnitArmorsParser(final Repository<Unit> unitsRepo,
            final Repository<Armor> armorsRepo) {
        super();

        this.unitsRepo = unitsRepo;
        this.armorsRepo = armorsRepo;
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
    public final UnitArmorAvailability parse(final Map<String, Object> input) {
        final Unit unit;
        final Collection<Armor> armors;
        final Collection<String> armorNames;
        final Armor initial;

        unit = unitsRepo.getCollection(new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit unit) {
                return unit.getName().equals(input.get("unit"));
            }

        }).iterator().next();

        armorNames = (Collection<String>) input.get("armors");
        armors = armorsRepo.getCollection(new Predicate<Armor>() {

            @Override
            public final boolean apply(final Armor armor) {
                return armorNames.contains(armor.getName());
            }

        });

        if (input.containsKey("initial")) {
            initial = armorsRepo.getCollection(new Predicate<Armor>() {

                @Override
                public final boolean apply(final Armor armor) {
                    return armor.getName().equals(input.get("initial"));
                }

            }).iterator().next();
        } else {
            initial = armorsRepo.getCollection(new Predicate<Armor>() {

                @Override
                public final boolean apply(final Armor armor) {
                    return armor.getName().equals("unarmored");
                }

            }).iterator().next();
        }

        // TODO: Use a service
        return new DefaultUnitArmorAvailability(unit, armors, initial);
    }

}
