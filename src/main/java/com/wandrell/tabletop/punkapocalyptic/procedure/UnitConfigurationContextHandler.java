package com.wandrell.tabletop.punkapocalyptic.procedure;

import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.procedure.DefaultConstraintValidator.ContextHandler;
import com.wandrell.tabletop.punkapocalyptic.util.tag.UnitAware;

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
