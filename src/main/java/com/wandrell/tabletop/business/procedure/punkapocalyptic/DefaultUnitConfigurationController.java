package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultUnitConfigurationController implements
        UnitConfigurationController {

    private final String           compulsoryError;
    private final DataModelService dataModelService;
    private Unit                   unit;
    private String                 validationMessage = "";

    public DefaultUnitConfigurationController(final String weaponError,
            final DataModelService dataModelService) {
        super();

        checkNotNull(weaponError, "Received a null pointer as error message");
        checkNotNull(dataModelService,
                "Received a null pointer as the data model service");

        this.compulsoryError = weaponError;
        this.dataModelService = dataModelService;
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
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;

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

        return valid;
    }

    private final String getCompulsoryWeaponsErrorMessageTemplate() {
        return compulsoryError;
    }

    private final DataModelService getDataModelService() {
        return dataModelService;
    }

    private final void setValidationMessage(final String message) {
        validationMessage = message;
    }

}
