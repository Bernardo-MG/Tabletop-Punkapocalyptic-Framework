package com.wandrell.tabletop.punkapocalyptic.procedure;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

import com.google.common.base.Predicate;
import com.wandrell.pattern.repository.QueryableRepository;
import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.event.ValueChangeListener;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitEvent;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.GangUnitsUpToLimitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.GangBuilderStatusChangedListener;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.GangChangedEvent;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.GangChangedListener;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.UnitChangedListener;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.MaxUnitsValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private Gang                                                                                   gang;
    private final GangListener                                                                     gangListener;
    private final ValueChangeListener                                                              listenerMaxUnits;
    private final EventListenerList                                                                listeners = new EventListenerList();
    private ValueBox                                                                               maxUnits;
    private RulesetService                                                                         serviceRuleset;
    private final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository;
    private Constraint                                                                             unitLimitConstraint;
    private final ConstraintValidator                                                              validator;

    {
        listenerMaxUnits = new ValueChangeListener() {

            @Override
            public final void valueChanged(final ValueChangeEvent event) {
                fireMaxUnitsChangedEvent(new ValueChangeEvent(this,
                        event.getOldValue(), event.getNewValue()));
            }

        };
    }

    {
        gangListener = new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent event) {
                final Collection<Constraint> constraints;
                final FactionUnitAvailability ava;

                ava = getFactionUnitAvailabilityRepository()
                        .getCollection(
                                new Predicate<FactionUnitAvailability>() {

                                    @Override
                                    public boolean apply(
                                            FactionUnitAvailability input) {
                                        return input
                                                .getUnit()
                                                .getName()
                                                .equals(event.getUnit()
                                                        .getName());
                                    }

                                }).iterator().next();

                constraints = ava.getConstraints();

                for (final Constraint constraint : constraints) {
                    getConstraintValidator().addConstraint(constraint);
                }

                fireUnitAddedEvent(event);
            }

            @Override
            public final void unitRemoved(final UnitEvent event) {
                FactionUnitAvailability ava;

                getConstraintValidator()
                        .addConstraint(getUnitLimitConstraint());

                for (final Unit unit : getGang().getUnits()) {
                    ava = getFactionUnitAvailabilityRepository()
                            .getCollection(
                                    new Predicate<FactionUnitAvailability>() {

                                        @Override
                                        public boolean apply(
                                                FactionUnitAvailability input) {
                                            return input.getUnit().getName()
                                                    .equals(unit.getName());
                                        }

                                    }).iterator().next();

                    for (final Constraint constraint : ava.getConstraints()) {
                        getConstraintValidator().addConstraint(constraint);
                    }
                }

                fireUnitRemovedEvent(event);
            }

        };
    }

    public DefaultGangBuilderManager(
            final ConstraintValidator validator,
            final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository,
            final RulesetService rulesetService) {
        super();

        checkNotNull(validator,
                "Received a null pointer as units constraint validator");
        checkNotNull(unitAvaRepository,
                "Received a null pointer as faction unit availability repository");
        checkNotNull(rulesetService,
                "Received a null pointer as rule set service");

        this.validator = validator;
        this.unitAvaRepository = unitAvaRepository;
        serviceRuleset = rulesetService;

        getConstraintValidator().addConstraint(getUnitLimitConstraint());
    }

    @Override
    public final void
            addGangChangedListener(final GangChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(GangChangedListener.class, listener);
    }

    @Override
    public final void addStatusChangedListener(
            final GangBuilderStatusChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(GangBuilderStatusChangedListener.class, listener);
    }

    @Override
    public final void
            addUnitChangedListener(final UnitChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(UnitChangedListener.class, listener);
    }

    @Override
    public final Gang getGang() {
        return gang;
    }

    @Override
    public final ValueBox getMaxUnits() {
        return maxUnits;
    }

    @Override
    public final Collection<Unit> getUnitOptions() {
        final Collection<Unit> result;
        final Collection<FactionUnitAvailability> avas;

        avas = getFactionUnitAvailabilityRepository().getCollection(
                new Predicate<FactionUnitAvailability>() {

                    @Override
                    public boolean apply(FactionUnitAvailability input) {
                        return input.getFaction().getName()
                                .equals(getGang().getFaction().getName());
                    }

                });
        result = new LinkedList<>();
        for (final FactionUnitAvailability ava : avas) {
            result.add(ava.getUnit());
        }

        return result;
    }

    @Override
    public final Collection<String> getValidationMessages() {
        return getConstraintValidator().getValidationMessages();
    }

    @Override
    public final void removeGangChangedListener(
            final GangChangedListener listener) {
        getListeners().remove(GangChangedListener.class, listener);
    }

    @Override
    public final void removeStatusChangedListener(
            final GangBuilderStatusChangedListener listener) {
        getListeners().remove(GangBuilderStatusChangedListener.class, listener);
    }

    @Override
    public final void removeUnitChangedListener(
            final UnitChangedListener listener) {
        getListeners().remove(UnitChangedListener.class, listener);
    }

    @Override
    public final void setGang(final Gang gang) {
        final GangChangedEvent event;

        checkNotNull(gang, "Received a null pointer as gang");

        event = new GangChangedEvent(this, this.gang, gang);

        if (this.gang != null) {
            this.gang.removeGangListener(getGangListener());
        }

        if (maxUnits != null) {
            maxUnits.removeValueChangeListener(getMaxUnitsChangeListener());
        }

        this.gang = gang;

        maxUnits = new MaxUnitsValueBox(gang, getRulesetService());
        unitLimitConstraint = new GangUnitsUpToLimitConstraint(gang, maxUnits,
                "too_many_units");

        this.gang.addGangListener(getGangListener());
        maxUnits.addValueChangeListener(getMaxUnitsChangeListener());

        fireGangChangedEvent(event);
    }

    @Override
    public final Boolean validate() {
        return getConstraintValidator().validate();
    }

    private final void fireGangChangedEvent(final GangChangedEvent event) {
        final GangChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(GangChangedListener.class);
        for (final GangChangedListener l : listnrs) {
            l.gangChanged(event);
        }
    }

    private final void fireMaxUnitsChangedEvent(final ValueChangeEvent event) {
        final GangBuilderStatusChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(
                GangBuilderStatusChangedListener.class);
        for (final GangBuilderStatusChangedListener l : listnrs) {
            l.maxUnitsChanged(event);
        }
    }

    private final void fireUnitAddedEvent(final UnitEvent event) {
        final UnitChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(UnitChangedListener.class);
        for (final UnitChangedListener l : listnrs) {
            l.unitAdded(event);
        }
    }

    private final void fireUnitRemovedEvent(final UnitEvent event) {
        final UnitChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(UnitChangedListener.class);
        for (final UnitChangedListener l : listnrs) {
            l.unitRemoved(event);
        }
    }

    private final ConstraintValidator getConstraintValidator() {
        return validator;
    }

    private final
            QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>>
            getFactionUnitAvailabilityRepository() {
        return unitAvaRepository;
    }

    private final GangListener getGangListener() {
        return gangListener;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final ValueChangeListener getMaxUnitsChangeListener() {
        return listenerMaxUnits;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Constraint getUnitLimitConstraint() {
        return unitLimitConstraint;
    }

}
