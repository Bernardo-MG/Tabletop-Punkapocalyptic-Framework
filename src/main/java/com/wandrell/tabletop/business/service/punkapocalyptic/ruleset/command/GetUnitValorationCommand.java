package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class GetUnitValorationCommand implements ReturnCommand<Integer> {

    private final Unit unit;

    public GetUnitValorationCommand(final Unit unit) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;
    }

    @Override
    public final Integer execute() {
        Integer cost;

        cost = getUnit().getBaseCost();

        for (final Weapon weapon : getUnit().getWeapons()) {
            cost += weapon.getCost();
        }

        if (getUnit().getArmor() != null) {
            cost += getUnit().getArmor().getCost();
        }

        return cost;
    }

    private final Unit getUnit() {
        return unit;
    }

}
