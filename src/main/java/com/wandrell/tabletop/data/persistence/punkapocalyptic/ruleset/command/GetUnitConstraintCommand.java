package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.UpToHalfPointsLimitUnitConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.UptToACountUnitConstraint;
import com.wandrell.tabletop.business.service.punkapocalyptic.PunkapocalypticLocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class GetUnitConstraintCommand implements
        ReturnCommand<ArmyBuilderUnitConstraint>, LocalizationServiceAware {

    private final String                       constraint;
    private PunkapocalypticLocalizationService serviceLocalization;
    private final String                       unit;

    public GetUnitConstraintCommand(final String constraint, final String unit) {
        super();

        this.constraint = constraint;
        this.unit = unit;
    }

    @Override
    public final ArmyBuilderUnitConstraint execute() {
        final ArmyBuilderUnitConstraint constraint;

        if (getConstraint().equals("unique")) {
            constraint = new UptToACountUnitConstraint(getUnit(),
                    getLocalizationService().getMessageString(
                            "unit_should_be_unique"), 1);
        } else if (getConstraint().equals("up_to_half_points")) {
            constraint = new UpToHalfPointsLimitUnitConstraint(getUnit(),
                    String.format(
                            getLocalizationService().getMessageString(
                                    "unit_should_be_up_to_half_points"),
                            getLocalizationService().getUnitNameString(
                                    getUnit())));
        } else {
            constraint = null;
        }

        return constraint;
    }

    @Override
    public void setLocalizationService(
            final PunkapocalypticLocalizationService service) {
        serviceLocalization = service;
    }

    protected final String getConstraint() {
        return constraint;
    }

    protected final PunkapocalypticLocalizationService getLocalizationService() {
        return serviceLocalization;
    }

    protected final String getUnit() {
        return unit;
    }

}
