package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.procedure.DefaultConstraintValidator.ContextHandler;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.GangAware;

public class GangBuilderContextHandler implements ContextHandler {

    private GangBuilderManager gangManager;

    public GangBuilderContextHandler() {
        super();
    }

    @Override
    public final void setContext(final Constraint constraint) {
        if (constraint instanceof GangAware) {
            ((GangAware) constraint).setGang(getGangBuilderManager().getGang());
        }
    }

    public final void
            setGangBuilderManager(final GangBuilderManager gangManager) {
        this.gangManager = gangManager;
    }

    private final GangBuilderManager getGangBuilderManager() {
        return gangManager;
    }

}
