package com.wandrell.tabletop.punkapocalyptic.service.ruleset.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.MutantUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;

public final class GetUnitValorationCommand implements ReturnCommand<Integer> {

    private final Unit unit;
    private Integer    valoration;

    public GetUnitValorationCommand(final Unit unit) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");

        this.unit = unit;
    }

    @Override
    public final void execute() {
        Integer valoration;

        valoration = getUnit().getBaseCost();

        for (final Weapon weapon : getUnit().getWeapons()) {
            valoration += weapon.getCost();
        }

        for (final Equipment equipment : getUnit().getEquipment()) {
            valoration += equipment.getCost();
        }

        if (getUnit().getArmor() != null) {
            valoration += getUnit().getArmor().getCost();
        }

        if (getUnit() instanceof MutantUnit) {
            for (final Mutation mutation : ((MutantUnit) getUnit())
                    .getMutations()) {
                valoration += mutation.getCost();
            }
        }

        if (getUnit() instanceof GroupedUnit) {
            valoration = valoration
                    * ((GroupedUnit) getUnit()).getGroupSize().getValue();
        }
    }

    @Override
    public final Integer getResult() {
        return valoration;
    }

    private final Unit getUnit() {
        return unit;
    }

}
