package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllUnitEquipmentCommand implements
        ReturnCommand<Map<String, Collection<Equipment>>> {

    private final Map<String, Equipment> equipment;

    public GetAllUnitEquipmentCommand(final Map<String, Equipment> equipment) {
        super();

        this.equipment = equipment;
    }

    @Override
    public final Map<String, Collection<Equipment>> execute() throws Exception {
        getEquipment();
        return new LinkedHashMap<>();
    }

    private final Map<String, Equipment> getEquipment() {
        return equipment;
    }

}
