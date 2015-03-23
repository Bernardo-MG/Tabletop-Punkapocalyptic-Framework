package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class RangedWeaponTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public RangedWeaponTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Element rulesNode;
        final String name;
        final Element strength;
        final Element penetration;
        final Element range;
        final Element distanceCMNode;
        final Element distanceInchNode;
        final Integer strengthShort;
        final Integer strengthMedium;
        final Integer strengthLong;
        final Integer penetrationShort;
        final Integer penetrationMedium;
        final Integer penetrationLong;
        final Integer distanceShortCM;
        final Integer distanceMediumCM;
        final Integer distanceLongCM;
        final Integer distanceShortInches;
        final Integer distanceMediumInches;
        final Integer distanceLongInches;
        final Integer cost;
        final Collection<String> rules;

        name = input.getChildText(ModelNodeConf.NAME);

        strength = input.getChild(ModelNodeConf.STRENGTH);
        strengthShort = Integer.parseInt(strength
                .getChildText(ModelNodeConf.SHORT));
        strengthMedium = Integer.parseInt(strength
                .getChildText(ModelNodeConf.MEDIUM));
        strengthLong = Integer.parseInt(strength
                .getChildText(ModelNodeConf.LONG));

        penetration = input.getChild(ModelNodeConf.PENETRATION);
        penetrationShort = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.SHORT));
        penetrationMedium = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.MEDIUM));
        penetrationLong = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.LONG));

        range = input.getChild(ModelNodeConf.RANGE);

        distanceInchNode = range.getChild(ModelNodeConf.INCHES);
        distanceShortInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.SHORT));
        distanceMediumInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.MEDIUM));
        distanceLongInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.LONG));

        distanceCMNode = range.getChild(ModelNodeConf.CENTIMETERS);
        distanceShortCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.SHORT));
        distanceMediumCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.MEDIUM));
        distanceLongCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.LONG));

        cost = Integer.parseInt(input.getChildText(ModelNodeConf.COST));

        rules = new LinkedList<>();
        rulesNode = input.getChild("rules");
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(rule.getText());
            }
        }

        transaction = new LinkedHashMap<>();
        transaction.put("name", name);

        transaction.put("strength_short", strengthShort);
        transaction.put("strength_medium", strengthMedium);
        transaction.put("strength_long", strengthLong);

        transaction.put("penetration_short", penetrationShort);
        transaction.put("penetration_medium", penetrationMedium);
        transaction.put("penetration_long", penetrationLong);

        transaction.put("distance_short_inches", distanceShortInches);
        transaction.put("distance_medium_inches", distanceMediumInches);
        transaction.put("distance_long_inches", distanceLongInches);

        transaction.put("distance_short_cm", distanceShortCM);
        transaction.put("distance_medium_cm", distanceMediumCM);
        transaction.put("distance_long_cm", distanceLongCM);

        transaction.put("cost", cost);

        transaction.put("rules", rules);

        return transaction;
    }

}
