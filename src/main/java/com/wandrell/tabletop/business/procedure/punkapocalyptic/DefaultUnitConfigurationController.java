package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.procedure.event.ProcedureValidationListener;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.UnitConfigurationListener;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultUnitConfigurationController implements
        UnitConfigurationController {

    private final String            compulsoryError;
    private final DataModelService  dataModelService;
    private final EventListenerList listeners         = new EventListenerList();
    private Unit                    unit;
    private String                  validationMessage = "";

    public DefaultUnitConfigurationController(final String compulsoryError,
            final DataModelService dataModelService) {
        super();

        checkNotNull(compulsoryError,
                "Received a null pointer as error message");
        checkNotNull(dataModelService,
                "Received a null pointer as the data model service");

        this.compulsoryError = compulsoryError;
        this.dataModelService = dataModelService;
    }

    @Override
    public final void addProcedureValidationListener(
            final ProcedureValidationListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(ProcedureValidationListener.class, listener);
    }

    @Override
    public final void addUnitConfigurationListener(
            final UnitConfigurationListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(UnitConfigurationListener.class, listener);
    }

    @Override
    public final Unit getUnit() {
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
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;

        fireUnitSelectedEvent(new UnitEvent(this, getUnit()));

        validate();
    }

    @Override
    public final Boolean validate() {
        final StringBuilder textErrors;
        final Boolean valid;
        final UnitWeaponAvailability availability;

        availability = getDataModelService().getUnitWeaponAvailability(
                getUnit().getUnitName());

        textErrors = new StringBuilder();
        if (getUnit().getWeapons().size() < availability.getMinWeapons()) {
            valid = false;
            textErrors.append(String.format(
                    getCompulsoryWeaponsErrorMessageTemplate(),
                    availability.getMinWeapons()));
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
        final UnitConfigurationListener[] listnrs;

        listnrs = getListeners().getListeners(UnitConfigurationListener.class);
        for (final UnitConfigurationListener l : listnrs) {
            l.unitSelected(evt);
        }
    }

    private final void fireValidationFailedEvent(final EventObject evt) {
        final ProcedureValidationListener[] listnrs;

        listnrs = getListeners()
                .getListeners(ProcedureValidationListener.class);
        for (final ProcedureValidationListener l : listnrs) {
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

    private final String getCompulsoryWeaponsErrorMessageTemplate() {
        return compulsoryError;
    }

    private final DataModelService getDataModelService() {
        return dataModelService;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final void setValidationMessage(final String message) {
        validationMessage = message;
    }

}
