package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;

public final class UnitArmorsTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public UnitArmorsTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Collection<Map<String, Object>> armors;
        final String unit;
        final String initial;
        final Element armorsNode;
        Map<String, Object> armor;

        unit = input.getChildText("unit");

        if (input.getChild("armor") != null) {
            initial = input.getChildText("armor");
        } else {
            initial = null;
        }

        armors = new LinkedList<>();
        armorsNode = input.getChild("armors");
        if (armorsNode != null) {
            for (final Element armorNode : armorsNode.getChildren()) {
                armor = new LinkedHashMap<>();
                armor.put("name", armorNode.getChildText("name"));
                armor.put("cost", armorNode.getChildText("cost"));

                armors.add(armor);
            }
        }

        transaction = new LinkedHashMap<>();
        transaction.put("unit", unit);
        if (initial != null) {
            transaction.put("initial", initial);
        }
        transaction.put("armors", armors);

        return transaction;
    }

}
