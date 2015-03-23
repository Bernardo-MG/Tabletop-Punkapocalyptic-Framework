package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;

public final class UnitEquipmentTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public UnitEquipmentTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final String unit;
        final Element equipmentNode;
        final Collection<String> equipment;

        unit = input.getChildText("unit");

        equipmentNode = input.getChild("equipment_pieces");
        equipment = new LinkedList<>();
        for (final Element piece : equipmentNode.getChildren()) {
            equipment.add(piece.getText());
        }

        transaction = new LinkedHashMap<>();
        transaction.put("unit", unit);
        transaction.put("equipment", equipment);

        return transaction;
    }

}
