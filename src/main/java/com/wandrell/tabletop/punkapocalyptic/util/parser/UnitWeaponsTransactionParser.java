package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;

public final class UnitWeaponsTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public UnitWeaponsTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final String unit;
        final Integer min;
        final Integer max;
        final Collection<String> weapons;
        final Collection<String> enhancements;
        final Element interval;
        final Element enhancementsNode;

        unit = input.getChildText("unit");

        interval = input.getChild("weapons_interval");

        min = Integer.parseInt(interval.getChildText("min_weapons"));
        max = Integer.parseInt(interval.getChildText("max_weapons"));

        weapons = new LinkedHashSet<>();
        for (final Element weapon : input.getChild("weapons").getChildren()) {
            weapons.add(weapon.getText());
        }
        enhancements = new LinkedHashSet<>();
        enhancementsNode = input.getChild("weapon_enhancements");
        if (enhancementsNode != null) {
            for (final Element enhancement : enhancementsNode.getChildren()) {
                enhancements.add(enhancement.getText());
            }
        }

        transaction = new LinkedHashMap<>();
        transaction.put("unit", unit);
        transaction.put("min_weapons", min);
        transaction.put("max_weapons", max);
        transaction.put("weapons", weapons);
        // TODO: Faction-based enhancements should be read from somewhere else
        transaction.put("enhancements", enhancements);

        return transaction;
    }

}
