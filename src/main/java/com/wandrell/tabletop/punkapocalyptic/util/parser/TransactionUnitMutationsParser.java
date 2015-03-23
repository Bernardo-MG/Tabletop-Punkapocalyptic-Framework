package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.conf.ConstraintsConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitMutationAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;

public final class TransactionUnitMutationsParser implements
        Parser<Map<String, Object>, UnitMutationAvailability> {

    private final Repository<Mutation> mutationsRepo;
    private final Repository<Unit>     unitsRepo;

    public TransactionUnitMutationsParser(final Repository<Unit> unitsRepo,
            final Repository<Mutation> mutationsRepo) {
        super();

        this.unitsRepo = unitsRepo;
        this.mutationsRepo = mutationsRepo;
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
    public final UnitMutationAvailability
            parse(final Map<String, Object> input) {
        final Unit unit;
        final Collection<Mutation> mutations;
        final Collection<String> mutationNames;
        Integer max;

        unit = unitsRepo.getCollection(new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit unit) {
                return unit.getName().equals(input.get("unit"));
            }

        }).iterator().next();

        mutationNames = (Collection<String>) input.get("white_list");
        mutations = mutationsRepo.getCollection(new Predicate<Mutation>() {

            @Override
            public final boolean apply(final Mutation mutation) {
                return mutationNames.contains(mutation.getName());
            }

        });

        max = (Integer) input.get("max_mutations");

        // TODO: Use a service
        return new DefaultUnitMutationAvailability(unit, max, mutations);
    }

}
