package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.util.command.ReturnCommand;

public final class GetMaxAllowedUnitsCommand implements ReturnCommand<Integer> {

    private static final Integer RANGE = 100;
    private static final Integer STEP  = 3;
    private final Gang           gang;

    public GetMaxAllowedUnitsCommand(final Gang gang) {
        super();

        this.gang = gang;
    }

    @Override
    public final Integer execute() {
        Integer value;
        Integer max;

        value = getGang().getValoration().getStoredValue();
        if (value == 0) {
            max = STEP;
        } else {
            max = 0;
            while (value > 0) {
                if (value > RANGE) {
                    value -= RANGE;
                } else {
                    value = 0;
                }
                max += STEP;
            }
        }

        return max;
    }

    private final Gang getGang() {
        return gang;
    }

}
