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
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultFactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;

public final class TransactionFactionUnitsParser implements
        Parser<Map<String, Object>, FactionUnitAvailability> {

    private final Repository<Faction> factionsRepo;
    private final Repository<Unit>    unitsRepo;

    public TransactionFactionUnitsParser(final Repository<Unit> unitsRepo,
            final Repository<Faction> factionsRepo) {
        super();

        this.unitsRepo = unitsRepo;
        this.factionsRepo = factionsRepo;
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
    public final FactionUnitAvailability parse(final Map<String, Object> input) {
        final Unit unit;
        final Faction faction;
        final Collection<Constraint> constraints;
        Constraint constr;

        unit = unitsRepo.getCollection(new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit unit) {
                return unit.getName().equals(input.get("unit"));
            }

        }).iterator().next();

        faction = factionsRepo.getCollection(new Predicate<Faction>() {

            @Override
            public final boolean apply(final Faction faction) {
                return faction.getName().equals(input.get("faction"));
            }

        }).iterator().next();

        constraints = new LinkedList<>();
        for (final Map<String, Object> constraint : (Collection<Map<String, Object>>) input
                .get("constraints")) {
            if (constraint.size() == 1) {
                constr = getConstraint(constraint.get("name").toString(),
                        unit.getName(), new LinkedList<>());
            } else {
                constr = getConstraint(constraint.get("name").toString(),
                        unit.getName(),
                        (Collection<String>) constraint.get("tags"));
            }

            constraints.add(constr);
        }

        // TODO: Use a service
        return new DefaultFactionUnitAvailability(faction, unit, constraints);
    }

}
