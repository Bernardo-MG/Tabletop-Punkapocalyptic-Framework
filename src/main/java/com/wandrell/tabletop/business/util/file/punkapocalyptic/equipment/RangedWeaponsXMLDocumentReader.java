package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.SpecialRuleNameConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultRangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class RangedWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Weapon>> {

    private final RulesetDAO daoRule;

    public RangedWeaponsXMLDocumentReader(final RulesetDAO dao) {
        super();

        daoRule = dao;
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
        Integer hands;
        Collection<SpecialRule> rules;
        Weapon weapon;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);

            strength = node.getChild(ModelNodeConf.STRENGTH);
            strengthShort = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.SHORT));
            strengthMedium = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.MEDIUM));
            strengthLong = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.LONG));

            penetration = node.getChild(ModelNodeConf.PENETRATION);
            penetrationShort = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.SHORT));
            penetrationMedium = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.MEDIUM));
            penetrationLong = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.LONG));

            range = node.getChild(ModelNodeConf.RANGE);

            distance = range.getChild(ModelNodeConf.INCHES);
            distanceShortInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.SHORT));
            distanceMediumInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.MEDIUM));
            distanceLongInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.LONG));

            distance = range.getChild(ModelNodeConf.CM);
            distanceShortCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.SHORT));
            distanceMediumCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.MEDIUM));
            distanceLongCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.LONG));

            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            rules = getRules(node.getChild(ModelNodeConf.RULES));

            if (rules.contains(getSpecialRuleDAO().getSpecialRule(
                    SpecialRuleNameConf.TWO_HANDED))) {
                hands = 2;
            } else {
                hands = 1;
            }

            weapon = new DefaultRangedWeapon(name, cost, hands,
                    penetrationShort, penetrationMedium, penetrationLong,
                    strengthShort, strengthMedium, strengthLong,
                    distanceShortCM, distanceMediumCM, distanceLongCM,
                    distanceShortInches, distanceMediumInches,
                    distanceLongInches, rules);

            weapons.put(name, weapon);
        }

        return weapons;
    }

    private final Collection<SpecialRule> getRules(final Element rulesNode) {
        Collection<SpecialRule> rules;

        rules = new LinkedList<>();
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(getSpecialRuleDAO().getSpecialRule(rule.getText()));
            }
        }

        return rules;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
