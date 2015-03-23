package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Map;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultEquipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;

public final class TransactionEquipmentParser implements
        Parser<Map<String, Object>, Equipment> {

    public TransactionEquipmentParser() {
        super();
    }

    @Override
    public final Equipment parse(final Map<String, Object> input) {
        // TODO: Use a service
        return new DefaultEquipment(input.get("name").toString(),
                (Integer) input.get("cost"));
    }

}
