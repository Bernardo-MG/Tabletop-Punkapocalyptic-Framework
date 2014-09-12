package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.business.procedure.event.ProcedureValidationListener;

public final class DefaultUnitConfigurationController implements
        UnitConfigurationController {

    private final String            compulsoryWeaponsError;
    private final EventListenerList listeners         = new EventListenerList();
    private AvailabilityUnit        unit;
    private String                  validationMessage = "";

    public DefaultUnitConfigurationController(
            final String compulsoryWeaponsError) {
        super();

        if (unit == null) {
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
    public final String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public final Collection<Weapon> getWeaponsSelection() {
        final Collection<Weapon> weapons;

        weapons = new LinkedList<>();
        for (final Weapon weapon : getUnit().getWeaponOptions()) {
            if ((!weapons.contains(weapon))
                    && (weapon.getHands() <= getUnit().getFreeWeaponSlots()
                            .getStoredValue())) {
                weapons.add(weapon);
            }
        }

        return weapons;
    }

    @Override
    public final void removeProcedureValidationListener(
            final ProcedureValidationListener listener) {
        getListeners().remove(ProcedureValidationListener.class, listener);
    }

    @Override
    public final void setUnit(final AvailabilityUnit unit) {
        if (unit == null) {
            throw new NullPointerException("Received a null pointer as unit");
        }

        this.unit = unit;

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

        if (valid) {
            fireValidationPassedEvent(new EventObject(this));
        } else {
            fireValidationFailedEvent(new EventObject(this));
        }

        return valid;
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

    protected final String getCompulsoryWeaponsErrorMessageTemplate() {
        return compulsoryWeaponsError;
    }

    protected final EventListenerList getListeners() {
        return listeners;
    }

    protected final AvailabilityUnit getUnit() {
        return unit;
    }

    protected final void setValidationMessage(final String message) {
        validationMessage = message;
    }

}
