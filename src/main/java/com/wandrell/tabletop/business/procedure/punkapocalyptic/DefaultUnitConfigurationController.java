package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import java.util.EventObject;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.procedure.event.ProcedureValidationListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.UnitConfigurationListener;

public final class DefaultUnitConfigurationController implements
        UnitConfigurationController {

    private final String            compulsoryWeaponsError;
    private final EventListenerList listeners         = new EventListenerList();
    private AvailabilityUnit        unit;
    private String                  validationMessage = "";

    public DefaultUnitConfigurationController(
            final String compulsoryWeaponsError) {
        super();

        if (compulsoryWeaponsError == null) {
            throw new NullPointerException(
                    "Received a null pointer as the compulsory weapons error message");
        }

        this.compulsoryWeaponsError = compulsoryWeaponsError;
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
    public final void addUnitConfigurationListener(
            final UnitConfigurationListener listener) {
        if (listener == null) {
            throw new NullPointerException(
                    "Received a null pointer as listener");
        }

        getListeners().add(UnitConfigurationListener.class, listener);
    }

    @Override
    public final AvailabilityUnit getUnit() {
        return unit;
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
    public final void removeUnitConfigurationListener(
            final UnitConfigurationListener listener) {
        getListeners().remove(UnitConfigurationListener.class, listener);
    }

    @Override
    public final void setUnit(final AvailabilityUnit unit) {
        if (unit == null) {
            throw new NullPointerException("Received a null pointer as unit");
        }

        this.unit = unit;

        fireUnitSelectedEvent(new UnitEvent(this, getUnit()));

        validate();
    }

    @Override
    public final Boolean validate() {
        final StringBuilder textErrors;
        final Boolean valid;

        textErrors = new StringBuilder();
        if (getUnit().getWeapons().size() < getUnit().getMinWeapons()) {
            valid = false;
            textErrors.append(String.format(
                    getCompulsoryWeaponsErrorMessageTemplate(), getUnit()
                            .getMinWeapons()));
        } else {
            valid = true;
        }

        setValidationMessage(textErrors.toString());

        if (valid) {
            fireValidationPassedEvent(new EventObject(this));
        } else {
            fireValidationFailedEvent(new EventObject(this));
        }

        return valid;
    }

    private final void fireUnitSelectedEvent(final UnitEvent evt) {
        final UnitConfigurationListener[] ls;

        if (evt == null) {
            throw new NullPointerException("Received a null pointer as event");
        }

        ls = getListeners().getListeners(UnitConfigurationListener.class);
        for (final UnitConfigurationListener l : ls) {
            l.unitSelected(evt);
        }
    }

    private final void fireValidationFailedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        if (evt == null) {
            throw new NullPointerException("Received a null pointer as event");
        }

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationFailed(evt);
        }
    }

    private final void fireValidationPassedEvent(final EventObject evt) {
        final ProcedureValidationListener[] ls;

        if (evt == null) {
            throw new NullPointerException("Received a null pointer as event");
        }

        ls = getListeners().getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : ls) {
            l.validationPassed(evt);
        }
    }

    private final String getCompulsoryWeaponsErrorMessageTemplate() {
        return compulsoryWeaponsError;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final void setValidationMessage(final String message) {
        validationMessage = message;
    }

}
