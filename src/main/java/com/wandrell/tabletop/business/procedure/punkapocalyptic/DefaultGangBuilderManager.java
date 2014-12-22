package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.availability.FactionUnitAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.model.valuebox.derived.DerivedValueBox;
import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.procedure.ConstraintValidator;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedEvent;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.UnitChangedListener;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataService;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private Gang                      gang;
    private final GangListener        gangListener;
    private final EventListenerList   listeners = new EventListenerList();
    private final DerivedValueBox     maxUnits;
    private final DataService         serviceModel;
    private RulesetService            serviceRuleset;
    private final Constraint          unitLimitConstraint;
    private final ConstraintValidator validator;

    {
        gangListener = new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent event) {
                final Collection<Constraint> constraints;

                constraints = getDataModelService().getUnitConstraints(
                        event.getUnit().getUnitName(),
                        getGang().getFaction().getName());

                for (final Constraint constraint : constraints) {
                    getConstraintValidator().addConstraint(constraint);
                }

                fireUnitAddedEvent(event);
            }

            @Override
            public final void unitRemoved(final UnitEvent event) {
                final Collection<Constraint> constraints;

                constraints = new LinkedList<>();
                for (final Unit unit : getGang().getUnits()) {
                    constraints.addAll(getDataModelService()
                            .getUnitConstraints(unit.getUnitName(),
                                    getGang().getFaction().getName()));
                }

                getConstraintValidator().setConstraints(constraints);
                getConstraintValidator()
                        .addConstraint(getUnitLimitConstraint());

                fireUnitRemovedEvent(event);
            }

        };
    }

    public DefaultGangBuilderManager(final Constraint unitLimitConstraint,
            final ConstraintValidator validator,
            final DerivedValueBox maxUnits, final DataService dataModelService,
            final RulesetService rulesetService) {
        super();

        checkNotNull(unitLimitConstraint,
                "Received a null pointer as units limit constraint");
        checkNotNull(validator,
                "Received a null pointer as units constraint validator");
        checkNotNull(maxUnits,
                "Received a null pointer as units limit value handler");
        checkNotNull(dataModelService,
                "Received a null pointer as model service");
        checkNotNull(rulesetService,
                "Received a null pointer as rule set service");

        this.maxUnits = maxUnits;

        this.unitLimitConstraint = unitLimitConstraint;
        this.validator = validator;
        this.serviceModel = dataModelService;
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
        final Collection<FactionUnitAvailability> units;
        final Collection<Unit> result;

        units = getGang().getFaction().getUnits();
        result = new LinkedList<>();
        for (final FactionUnitAvailability unit : units) {
            result.add(unit.getUnit());
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

        getRulesetService().setUpMaxUnitsValueHandler(
                (DerivedValueBox) getMaxUnits(), getGang());

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

    private final DataService getDataModelService() {
        return serviceModel;
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
