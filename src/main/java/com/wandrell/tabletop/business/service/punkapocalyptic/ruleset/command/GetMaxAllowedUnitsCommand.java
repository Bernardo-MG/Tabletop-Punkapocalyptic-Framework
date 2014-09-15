package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.util.command.ReturnCommand;

public final class GetMaxAllowedUnitsCommand implements ReturnCommand<Integer> {

    private final Gang gang;

    public GetMaxAllowedUnitsCommand(final Gang gang) {
        super();

        this.gang = gang;
    }

    @Override
    public final Integer execute() {
        return ((getGang().getValoration().getStoredValue() % 100) * 3);
    }

    protected final Gang getGang() {
        return gang;
    }

}
