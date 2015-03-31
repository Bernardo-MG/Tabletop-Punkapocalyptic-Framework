package com.wandrell.tabletop.punkapocalyptic.service.ruleset.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.pattern.command.ResultCommand;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;

public final class GetMaxAllowedUnitsCommand implements ResultCommand<Integer> {

    private static final Integer RANGE = 100;
    private static final Integer STEP  = 3;
    private final Gang           gang;
    private Integer              max;

    public GetMaxAllowedUnitsCommand(final Gang gang) {
        super();

        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;
    }

    @Override
    public final void execute() {
        Integer value;

        value = getGang().getValoration();
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
    }

    @Override
    public final Integer getResult() {
        return max;
    }

    private final Gang getGang() {
        return gang;
    }

}
