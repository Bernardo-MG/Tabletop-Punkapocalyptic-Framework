package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ConstraintsConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.MessageBundleKey;
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

        switch (getConstraint()) {
            case ConstraintsConf.UNIQUE:
                constraint = getUniqueConstraint();
                break;
            case ConstraintsConf.UP_TO_HALF_POINTS:
                constraint = getUpToHalfPointsConstraint();
                break;
            default:
                constraint = null;
        }

        return constraint;
    }

    @Override
    public void setLocalizationService(
            final PunkapocalypticLocalizationService service) {
        serviceLocalization = service;
    }

    private final ArmyBuilderUnitConstraint getUniqueConstraint() {
        final String message;

        message = getLocalizationService().getMessageString(
                MessageBundleKey.UNIT_SHOULD_BE_UNIQUE);

        return new UptToACountUnitConstraint(getUnit(), message, 1);
    }

    private final ArmyBuilderUnitConstraint getUpToHalfPointsConstraint() {
        final String message;

        message = String.format(
                getLocalizationService().getMessageString(
                        MessageBundleKey.UNIT_SHOULD_BE_UP_TO_HALF_POINTS),
                getLocalizationService().getUnitNameString(getUnit()));

        return new UpToHalfPointsLimitUnitConstraint(getUnit(), message);
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
