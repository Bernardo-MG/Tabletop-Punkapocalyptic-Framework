package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.procedure.constraint.punkapocalyptic.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuehandler.AbstractValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedEvent;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.UnitChangedListener;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private final Collection<GangConstraint> constraints        = new LinkedHashSet<>();
    private Gang                             gang;
    private final GangListener               gangListener;
    private final EventListenerList          listeners          = new EventListenerList();
    private final ModularDerivedValueHandler maxUnits;
    private final DataModelService           serviceModel;
    private RulesetService                   serviceRuleset;
    private final GangConstraint             unitLimitConstraint;
    private final Collection<String>         validationMessages = new LinkedHashSet<>();

    public DefaultGangBuilderManager(final GangConstraint unitLimitConstraint,
            final ModularDerivedValueHandler maxUnits,
            final DataModelService dataModelService,
            final RulesetService rulesetService) {
        super();

        checkNotNull(unitLimitConstraint,
                "Received a null pointer as units limit constraint");
        checkNotNull(maxUnits,
                "Received a null pointer as units units limit value handler");
        checkNotNull(dataModelService,
                "Received a null pointer as model service");
        checkNotNull(rulesetService,
                "Received a null pointer as rule set service");

        this.maxUnits = maxUnits;

        this.unitLimitConstraint = unitLimitConstraint;
        this.serviceModel = dataModelService;
        serviceRuleset = rulesetService;

        gangListener = new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent event) {
                getConstraints().addAll(
                        getDataModelService().getUnitConstraints(
                                event.getUnit().getUnitName(),
                                getGang().getFaction().getName()));

                validate();

                fireUnitAddedEvent(event);
            }

            @Override
            public final void unitRemoved(final UnitEvent event) {
                getConstraints().clear();

                for (final Unit unit : getGang().getUnits()) {
                    getConstraints().addAll(
                            getDataModelService().getUnitConstraints(
                                    unit.getUnitName(),
                                    getGang().getFaction().getName()));
                }

                validate();

                fireUnitRemovedEvent(event);
            }

        };
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
    public final ValueHandler getMaxUnits() {
        return maxUnits;
    }

    @Override
    public final Collection<Unit> getUnitOptions() {
        return getDataModelService().getFactionUnits(
                getGang().getFaction().getName());
    }

    @Override
    public final Collection<String> getValidationMessages() {
        return validationMessages;
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
        checkNotNull(gang, "Received a null pointer as gang");

        fireGangChangedEvent(new GangChangedEvent(this, this.gang, gang));

        if (this.gang != null) {
            this.gang.removeGangListener(getGangListener());
        }

        this.gang = gang;

        getRulesetService().setUpMaxUnitsValueHandler(
                (ModularDerivedValueHandler) getMaxUnits(), getGang());

        ((AbstractValueHandler) gang.getBullets())
                .addValueEventListener(new ValueHandlerListener() {

                    @Override
                    public final void valueChanged(final ValueHandlerEvent evt) {
                        validate();
                    }

                });

        gang.addGangListener(getGangListener());
    }

    @Override
    public final Boolean validate() {
        final Boolean count;
        final Boolean constraints;
        final Boolean valid;

        count = validateUnitsCount();

        constraints = validateUnitConstraints();

        valid = (count && constraints);

        if (valid) {
            getValidationMessages().clear();
        }

        return valid;
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

    private final Collection<GangConstraint> getConstraints() {
        return constraints;
    }

    private final DataModelService getDataModelService() {
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

    private final GangConstraint getUnitLimitConstraint() {
        return unitLimitConstraint;
    }

    private final Boolean validateUnitConstraints() {
        Boolean valid;

        valid = false;
        for (final GangConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getGang())) {
                getValidationMessages().add(constraint.getErrorMessage());

                valid = true;
            }
        }

        return valid;
    }

    private final Boolean validateUnitsCount() {
        final Boolean valid;

        valid = getUnitLimitConstraint().isValid(getGang());

        if (!valid) {
            getValidationMessages().add(
                    String.format(getUnitLimitConstraint().getErrorMessage(),
                            getGang().getUnits().size(), getMaxUnits()
                                    .getValue()));
        }

        return valid;
    }

}
