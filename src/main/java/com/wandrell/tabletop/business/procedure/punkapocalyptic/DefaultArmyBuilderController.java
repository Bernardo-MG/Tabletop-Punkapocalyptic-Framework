package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedHashSet;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuehandler.AbstractValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;
import com.wandrell.tabletop.business.procedure.event.ProcedureValidationListener;

public final class DefaultArmyBuilderController implements
        ArmyBuilderController {

    private Collection<ArmyBuilderUnitConstraint> constraints       = new LinkedHashSet<>();
    private final Gang                            gang;
    private final EventListenerList               listeners         = new EventListenerList();
    private final UnitConfigurationController     unitValidator;
    private String                                validationMessage = "";

    public DefaultArmyBuilderController(
            final UnitConfigurationController unitValidator, final Gang failed) {
        super();

        this.unitValidator = unitValidator;
        this.gang = failed;

        ((AbstractValueHandler) gang.getBullets())
                .addValueEventListener(new ValueHandlerListener() {

                    @Override
                    public final void valueChanged(final ValueHandlerEvent evt) {
                        validate();
                    }

                });

        gang.addGangListener(new GangListener() {

            @Override
            public final void unitAdded(final UnitEvent e) {
                getConstraints().addAll(
                        ((AvailabilityUnit) e.getUnit()).getConstraints());

                validate();
            }

            @Override
            public final void unitRemoved(final UnitEvent e) {
                getConstraints().clear();
                for (final Unit unit : getGang().getUnits()) {
                    getConstraints().addAll(
                            ((AvailabilityUnit) unit).getConstraints());
                }

                validate();
            }

        });
    }

    @Override
    public final void addProcedureValidationListener(
            final ProcedureValidationListener listener) {
        if (listener == null) {
            throw new NullPointerException(
                    "Received a null pointer as listener");
        }

        getListeners().add(ProcedureValidationListener.class, listener);
    }

    @Override
    public final Gang getGang() {
        return gang;
    }

    @Override
    public final UnitConfigurationController getUnitConfigurationController() {
        return unitValidator;
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
        Boolean failed;

        failed = false;
        textErrors = new StringBuilder();
        for (final ArmyBuilderUnitConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getGang())) {
                if (textErrors.toString().length() > 0) {
                    textErrors.append(System.lineSeparator());
                }
                textErrors.append(constraint.getErrorMessage());

                failed = true;
            }
        }

        setValidationMessage(textErrors.toString());

        if (failed) {
            fireValidationFailedEvent(new EventObject(this));
        } else {
            fireValidationPassedEvent(new EventObject(this));
        }

        return !failed;
    }

    protected final void fireValidationFailedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        if (evt == null) {
            throw new NullPointerException("Received a null pointer as event");
        }

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationFailed(evt);
        }
    }

    protected final void fireValidationPassedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        if (evt == null) {
            throw new NullPointerException("Received a null pointer as event");
        }

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationPassed(evt);
        }
    }

    protected final Collection<ArmyBuilderUnitConstraint> getConstraints() {
        return constraints;
    }

    protected final EventListenerList getListeners() {
        return listeners;
    }

    protected final void setValidationMessage(final String message) {
        validationMessage = message;
    }

}
