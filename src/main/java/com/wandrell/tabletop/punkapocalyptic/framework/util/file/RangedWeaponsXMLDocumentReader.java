package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.punkapocalyptic.inventory.DefaultRangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class RangedWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Weapon>> {

    private final Map<String, SpecialRule> rules;

    public RangedWeaponsXMLDocumentReader(final Map<String, SpecialRule> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Weapon> getValue(final Document doc) {
        final Element root;
        final Map<String, Weapon> weapons;
        String name;
        Element strength;
        Element penetration;
        Element range;
        Element distance;
        Integer strengthShort;
        Integer strengthMedium;
        Integer strengthLong;
        Integer penetrationShort;
        Integer penetrationMedium;
        Integer penetrationLong;
        Integer distanceShortCM;
        Integer distanceMediumCM;
        Integer distanceLongCM;
        Integer distanceShortInches;
        Integer distanceMediumInches;
        Integer distanceLongInches;
        Integer cost;
        Collection<SpecialRule> rules;
        Weapon weapon;
        Element rulesNode;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText("name");

            strength = node.getChild("strength");
            strengthShort = Integer.parseInt(strength.getChildText("short"));
            strengthMedium = Integer.parseInt(strength.getChildText("medium"));
            strengthLong = Integer.parseInt(strength.getChildText("long"));

            penetration = node.getChild("penetration");
            penetrationShort = Integer.parseInt(penetration
                    .getChildText("short"));
            penetrationMedium = Integer.parseInt(penetration
                    .getChildText("medium"));
            penetrationLong = Integer
                    .parseInt(penetration.getChildText("long"));

            range = node.getChild("range");

            distance = range.getChild("inches");
            distanceShortInches = Integer.parseInt(distance
                    .getChildText("short"));
            distanceMediumInches = Integer.parseInt(distance
                    .getChildText("medium"));
            distanceLongInches = Integer
                    .parseInt(distance.getChildText("long"));

            distance = range.getChild("cm");
            distanceShortCM = Integer.parseInt(distance.getChildText("short"));
            distanceMediumCM = Integer
                    .parseInt(distance.getChildText("medium"));
            distanceLongCM = Integer.parseInt(distance.getChildText("long"));

            cost = Integer.parseInt(node.getChildText("cost"));

            rules = new LinkedList<>();

            rulesNode = node.getChild("rules");
            if (rulesNode != null) {
                for (final Element rule : node.getChild("rules").getChildren()) {
                    rules.add(getRules().get(rule.getText()));
                }
            }

            weapon = new DefaultRangedWeapon(name, cost, penetrationShort,
                    penetrationMedium, penetrationLong, strengthShort,
                    strengthMedium, strengthLong, distanceShortCM,
                    distanceMediumCM, distanceLongCM, distanceShortInches,
                    distanceMediumInches, distanceLongInches, rules);

            weapons.put(name, weapon);
        }

        return weapons;
    }

    protected final Map<String, SpecialRule> getRules() {
        return rules;
    }

}
