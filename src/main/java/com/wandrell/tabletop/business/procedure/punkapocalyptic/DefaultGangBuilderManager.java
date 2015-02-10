package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.swing.event.EventListenerList;

import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.FactionUnitAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.procedure.ConstraintValidator;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedEvent;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.UnitChangedListener;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private Gang                                      gang;
    private final GangListener                        gangListener;
    private final EventListenerList                   listeners = new EventListenerList();
    private final ValueBox                            maxUnits;
    private RulesetService                            serviceRuleset;
    private final Repository<FactionUnitAvailability> unitAvaRepository;
    private final Constraint                          unitLimitConstraint;
    private final ConstraintValidator                 validator;

    {
        gangListener = new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent event) {
                final Collection<Constraint> constraints;
                final FactionUnitAvailability ava;

                ava = getFactionUnitAvailabilityRepository()
                        .getCollection(
                                a -> a.getUnit().getUnitName()
                                        .equals(event.getUnit().getUnitName()))
                        .iterator().next();

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
                                    a -> a.getUnit().getUnitName()
                                            .equals(unit.getUnitName()))
                            .iterator().next();

                    for (final Constraint constraint : ava.getConstraints()) {
                        getConstraintValidator().addConstraint(constraint);
                    }
                }

                fireUnitRemovedEvent(event);
            }

        };
    }

    public DefaultGangBuilderManager(final Constraint unitLimitConstraint,
            final ConstraintValidator validator, final ValueBox maxUnits,
            final Repository<FactionUnitAvailability> unitAvaRepository,
            final RulesetService rulesetService) {
        super();

        checkNotNull(unitLimitConstraint,
                "Received a null pointer as units limit constraint");
        checkNotNull(validator,
                "Received a null pointer as units constraint validator");
        checkNotNull(maxUnits,
                "Received a null pointer as units limit value handler");
        checkNotNull(unitAvaRepository,
                "Received a null pointer as faction unit availability repository");
        checkNotNull(rulesetService,
                "Received a null pointer as rule set service");

        this.maxUnits = maxUnits;

        this.unitLimitConstraint = unitLimitConstraint;
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

        result = getFactionUnitAvailabilityRepository()
                .getCollection(
                        a -> a.getFaction().getName()
                                .equals(getGang().getFaction().getName()))
                .stream().map(a -> a.getUnit()).collect(Collectors.toList());

        return result;
    }

    @Override
    public final Collection<String> getValidationMessages() {
        return getConstraintValidator().getValidationMessages();
    }

    @Override
    public final void removeGangChangedListener(
            final GangChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().remove(GangChangedListener.class, listener);
    }

    @Override
    public final void removeUnitChangedListener(
            final UnitChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

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

        this.gang = gang;

        getRulesetService().setUpMaxUnitsValueHandler(getMaxUnits(), getGang());

        gang.addGangListener(getGangListener());

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

    private final Repository<FactionUnitAvailability>
            getFactionUnitAvailabilityRepository() {
        return unitAvaRepository;
    }

    private final GangListener getGangListener() {
        return gangListener;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Constraint getUnitLimitConstraint() {
        return unitLimitConstraint;
    }

}
