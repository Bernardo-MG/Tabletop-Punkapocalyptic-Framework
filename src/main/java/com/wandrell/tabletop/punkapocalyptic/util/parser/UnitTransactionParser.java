package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class UnitTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public UnitTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Element rulesNode;
        final String name;
        final Integer actions;
        final Integer combat;
        final Integer precision;
        final Integer agility;
        final Integer strength;
        final Integer toughness;
        final Integer tech;
        final Integer cost;
        final Collection<String> rules;

        name = input.getChildText(ModelNodeConf.NAME);

        actions = Integer.parseInt(input.getChildText(ModelNodeConf.ACTIONS));
        combat = Integer.parseInt(input.getChildText(ModelNodeConf.COMBAT));
        precision = Integer.parseInt(input
                .getChildText(ModelNodeConf.PRECISION));
        agility = Integer.parseInt(input.getChildText(ModelNodeConf.AGILITY));
        strength = Integer.parseInt(input.getChildText(ModelNodeConf.STRENGTH));
        toughness = Integer.parseInt(input
                .getChildText(ModelNodeConf.TOUGHNESS));
        tech = Integer.parseInt(input.getChildText(ModelNodeConf.TECH));

        cost = Integer.parseInt(input.getChildText(ModelNodeConf.COST));

        rules = new LinkedList<>();
        // TODO: Rules should be taken from somewhere else
        // TODO: This requires an XQuery
        rulesNode = input.getChild("rules");
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(rule.getText());
            }
        }

        transaction = new LinkedHashMap<>();

        transaction.put("name", name);
        transaction.put("actions", actions);
        transaction.put("combat", combat);
        transaction.put("precision", precision);
        transaction.put("agility", agility);
        transaction.put("strength", strength);
        transaction.put("toughness", toughness);
        transaction.put("tech", tech);
        transaction.put("cost", cost);
        transaction.put("rules", rules);

        return transaction;
    }

}
