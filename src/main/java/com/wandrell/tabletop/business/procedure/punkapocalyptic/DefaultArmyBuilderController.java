package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedHashSet;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuehandler.AbstractValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;
import com.wandrell.tabletop.business.procedure.event.ProcedureValidationListener;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultArmyBuilderController implements
        ArmyBuilderController {

    private Collection<GangConstraint>        constraints       = new LinkedHashSet<>();
    private final UnitConfigurationController controller;
    private final Gang                        gang;
    private final EventListenerList           listeners         = new EventListenerList();
    private final ValueHandler                maxUnits;
    private final DataModelService            serviceModel;
    private final String                      tooManyUnitsMessage;
    private String                            validationMessage = "";

    public DefaultArmyBuilderController(
            final UnitConfigurationController controller, final Gang gang,
            final ValueHandler maxUnits, final String tooManyUnitsMessage,
            final DataModelService serviceModel) {
        super();

        checkNotNull(controller, "Received a null pointer as controller");
        checkNotNull(gang, "Received a null pointer as gang");
        checkNotNull(maxUnits, "Received a null pointer as max units");
        checkNotNull(tooManyUnitsMessage, "Received a null pointer as message");
        checkNotNull(serviceModel, "Received a null pointer as model service");

        this.controller = controller;
        this.gang = gang;
        this.maxUnits = maxUnits;
        this.tooManyUnitsMessage = tooManyUnitsMessage;
        this.serviceModel = serviceModel;

        ((AbstractValueHandler) gang.getBullets())
                .addValueEventListener(new ValueHandlerListener() {

                    @Override
                    public final void valueChanged(final ValueHandlerEvent evt) {
                        validate();
                    }

                });

        gang.addGangListener(new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent e) {
                getConstraints().addAll(
                        getDataModelService().getUnitConstraints(
                                e.getUnit().getUnitName(),
                                getGang().getFaction().getName()));

                validate();
            }

            @Override
            public final void unitRemoved(final UnitEvent e) {
                getConstraints().clear();
                for (final Unit unit : getGang().getUnits()) {
                    getConstraints().addAll(
                            getDataModelService().getUnitConstraints(
                                    unit.getUnitName(),
                                    getGang().getFaction().getName()));
                }

                validate();
            }

        });
    }

    @Override
    public final void addProcedureValidationListener(
            final ProcedureValidationListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(ProcedureValidationListener.class, listener);
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
    public final UnitConfigurationController getUnitConfigurationController() {
        return controller;
    }

    @Override
    public final String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public final void removeProcedureValidationListener(
            final ProcedureValidationListener listener) {
        getListeners().remove(ProcedureValidationListener.class, listener);
    }

    @Override
    public final Boolean validate() {
        final StringBuilder textErrors;
        final Boolean failedCount;
        final Boolean failedConstraints;
        final Boolean failed;

        textErrors = new StringBuilder();

        failedCount = validateUnitsCount(textErrors);

        failedConstraints = validateUnitConstraints(textErrors);

        failed = (failedCount || failedConstraints);

        setValidationMessage(textErrors.toString());

        if (failed) {
            fireValidationFailedEvent(new EventObject(this));
        } else {
            fireValidationPassedEvent(new EventObject(this));
        }

        return !failed;
    }

    private final void fireValidationFailedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationFailed(evt);
        }
    }

    private final void fireValidationPassedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationPassed(evt);
        }
    }

    private final Collection<GangConstraint> getConstraints() {
        return constraints;
    }

    private final DataModelService getDataModelService() {
        return serviceModel;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final String getTooManyUnitsWarningMessage() {
        return tooManyUnitsMessage;
    }

    private final void setValidationMessage(final String message) {
        validationMessage = message;
    }

    private final Boolean
            validateUnitConstraints(final StringBuilder textErrors) {
        Boolean failed;

        failed = false;
        for (final GangConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getGang())) {
                if (textErrors.toString().length() > 0) {
                    textErrors.append(System.lineSeparator());
                }
                textErrors.append(constraint.getErrorMessage());

                failed = true;
            }
        }

        return failed;
    }

    private final Boolean validateUnitsCount(final StringBuilder textErrors) {
        final Boolean failed;

        if (getGang().getUnits().size() > getMaxUnits().getValue()) {
            textErrors.append(String.format(getTooManyUnitsWarningMessage(),
                    getGang().getUnits().size(), getMaxUnits().getValue()));
            failed = true;
        } else {
            failed = false;
        }

        return failed;
    }

}
