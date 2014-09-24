package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ConstraintsConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllGangConstraintsCommand implements
        ReturnCommand<Map<String, GangConstraint>>, LocalizationServiceAware {

    private LocalizationService serviceLocalization;

    public GetAllGangConstraintsCommand() {
        super();
    }

    @Override
    public final Map<String, GangConstraint> execute() {
        final Map<String, GangConstraint> constraints;

        constraints = new LinkedHashMap<>();
        constraints.put(ConstraintsConf.UNIQUE, getUniqueConstraint());
        constraints.put(ConstraintsConf.UP_TO_HALF_POINTS,
                getUpToHalfPointsConstraint());

        return constraints;
    }

    @Override
    public void setLocalizationService(final LocalizationService service) {
        serviceLocalization = service;
    }

    private final LocalizationService getLocalizationService() {
        return serviceLocalization;
    }

    private final GangConstraint getUniqueConstraint() {
        return new UnitUpToACountConstraint(1, getLocalizationService());
    }

    private final GangConstraint getUpToHalfPointsConstraint() {
        return new UnitUpToHalfGangLimitConstraint(getLocalizationService());
    }

}
