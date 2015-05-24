package com.wandrell.tabletop.punkapocalyptic.procedure;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.procedure.ConstraintData;
import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.procedure.DefaultConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitEvent;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.GangUnitsUpToLimitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.GangBuilderStatusChangedListener;
import com.wandrell.tabletop.punkapocalyptic.procedure.event.GangChangedEvent;
import com.wandrell.tabletop.punkapocalyptic.repository.FactionUnitAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.MaxUnitsValueBox;
import com.wandrell.tabletop.stats.event.ValueChangeEvent;
import com.wandrell.tabletop.stats.event.ValueChangeListener;
import com.wandrell.tabletop.stats.valuebox.ValueBox;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private Gang                                    gang;
    private final GangListener                      gangListener;
    private final DefaultGangBuilderOptions         gangOptions;
    private final ValueChangeListener               listenerMaxUnits;
    private final EventListenerList                 listeners = new EventListenerList();
    private ValueBox                                maxUnits;
    private final ModelService                      serviceModel;
    private final RulesetService                    serviceRuleset;
    private final FactionUnitAvailabilityRepository unitAvaRepository;
    private Constraint                              unitLimitConstraint;
    private final ConstraintValidator               validator;

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
                final FactionUnitAvailability ava;
                Constraint constraint;

                ava = getFactionUnitAvailabilityRepository()
                        .getAvailabilityForUnit(
                                getGang().getFaction().getNameToken(),
                                event.getUnit().getUnitTemplate()
                                        .getNameToken());

                for (final ConstraintData data : ava.getConstraints()) {
                    constraint = getModelService().getConstraint(getGang(),
                            data.getNameToken(), ava.getUnit().getNameToken(),
                            data.getContext());
                    getConstraintValidator().addConstraint(constraint);
                }

                fireUnitAddedEvent(event);
            }

            @Override
            public final void unitRemoved(final UnitEvent event) {
                Constraint constraint;
                FactionUnitAvailability ava;

                getConstraintValidator()
                        .addConstraint(getUnitLimitConstraint());

                for (final Unit unit : getGang().getUnits()) {
                    ava = getFactionUnitAvailabilityRepository()
                            .getAvailabilityForUnit(
                                    getGang().getFaction().getNameToken(),
                                    unit.getUnitTemplate().getNameToken());

                    for (final ConstraintData data : ava.getConstraints()) {
                        constraint = getModelService()
                                .getConstraint(getGang(), data.getNameToken(),
                                        ava.getUnit().getNameToken(),
                                        data.getContext());
                        getConstraintValidator().addConstraint(constraint);
                    }
                }

                fireUnitRemovedEvent(event);
            }

        };
    }

    public DefaultGangBuilderManager(
            final FactionUnitAvailabilityRepository unitAvaRepository,
            final RulesetService rulesetService, final ModelService modelService) {
        super();

        checkNotNull(unitAvaRepository,
                "Received a null pointer as faction unit availability repository");
        checkNotNull(rulesetService,
                "Received a null pointer as ruleset service");
        checkNotNull(modelService, "Received a null pointer as model service");

        this.validator = new DefaultConstraintValidator();
        this.unitAvaRepository = unitAvaRepository;
        serviceRuleset = rulesetService;
        serviceModel = modelService;

        gangOptions = new DefaultGangBuilderOptions(unitAvaRepository);
    }

    @Override
    public final void addStatusChangedListener(
            final GangBuilderStatusChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(GangBuilderStatusChangedListener.class, listener);
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
    public final DefaultGangBuilderOptions getOptions() {
        return gangOptions;
    }

    @Override
    public final Collection<String> getValidationMessages() {
        return getConstraintValidator().getValidationMessages();
    }

    @Override
    public final void removeStatusChangedListener(
            final GangBuilderStatusChangedListener listener) {
        getListeners().remove(GangBuilderStatusChangedListener.class, listener);
    }

    @Override
    public final void setGang(final Gang gang) {
        final GangChangedEvent event;

        checkNotNull(gang, "Received a null pointer as gang");

        event = new GangChangedEvent(this, this.gang, gang);

        if (this.gang != null) {
            this.gang.removeGangListener(getGangListener());
        }

        getOptions().setFactionName(gang.getFaction().getNameToken());

        if (maxUnits != null) {
            maxUnits.removeValueChangeListener(getMaxUnitsChangeListener());
        }

        this.gang = gang;

        maxUnits = new MaxUnitsValueBox(gang, getRulesetService());
        unitLimitConstraint = new GangUnitsUpToLimitConstraint(gang, maxUnits,
                "too_many_units");
        getConstraintValidator().addConstraint(getUnitLimitConstraint());

        this.gang.addGangListener(getGangListener());
        maxUnits.addValueChangeListener(getMaxUnitsChangeListener());

        fireGangChangedEvent(event);
    }

    @Override
    public final Boolean validate() {
        return getConstraintValidator().validate();
    }

    private final void fireGangChangedEvent(final GangChangedEvent event) {
        final GangBuilderStatusChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(
                GangBuilderStatusChangedListener.class);
        for (final GangBuilderStatusChangedListener l : listnrs) {
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
        final GangBuilderStatusChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(
                GangBuilderStatusChangedListener.class);
        for (final GangBuilderStatusChangedListener l : listnrs) {
            l.unitAdded(event);
        }
    }

    private final void fireUnitRemovedEvent(final UnitEvent event) {
        final GangBuilderStatusChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(
                GangBuilderStatusChangedListener.class);
        for (final GangBuilderStatusChangedListener l : listnrs) {
            l.unitRemoved(event);
        }
    }

    private final ConstraintValidator getConstraintValidator() {
        return validator;
    }

    private final FactionUnitAvailabilityRepository
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

    private final ModelService getModelService() {
        return serviceModel;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Constraint getUnitLimitConstraint() {
        return unitLimitConstraint;
    }

}
