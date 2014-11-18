package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.procedure.DefaultConstraintValidator.ContextHandler;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.UnitAware;

public class UnitConfigurationContextHandler implements ContextHandler {

    private UnitConfigurationManager unitManager;

    public UnitConfigurationContextHandler() {
        super();
    }

    @Override
    public final void setContext(final Constraint constraint) {
        if (constraint instanceof UnitAware) {
            ((UnitAware) constraint).setUnit(getUnitConfigurationManager()
                    .getUnit());
        }
    }

    public final void setUnitConfigurationManager(
            final UnitConfigurationManager unitManager) {
        this.unitManager = unitManager;
    }

    private final UnitConfigurationManager getUnitConfigurationManager() {
        return unitManager;
    }

}
