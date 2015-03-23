package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class MutationTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public MutationTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Element bonusList;
        final String name;
        final Integer cost;
        Integer actions = 0;
        Integer agility = 0;
        Integer combat = 0;
        Integer precision = 0;
        Integer strength = 0;
        Integer tech = 0;
        Integer toughness = 0;

        name = input.getChildText(ModelNodeConf.NAME);
        cost = Integer.parseInt(input.getChildText(ModelNodeConf.COST));

        bonusList = input.getChild(ModelNodeConf.BONUS);
        if (bonusList != null) {
            for (final Element bonus : bonusList.getChildren()) {
                switch (bonus.getName()) {
                    case ModelNodeConf.ACTIONS:
                        actions = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.AGILITY:
                        agility = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.COMBAT:
                        combat = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.PRECISION:
                        precision = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.STRENGTH:
                        strength = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.TECH:
                        tech = Integer.parseInt(bonus.getText());
                        break;
                    case ModelNodeConf.TOUGHNESS:
                        toughness = Integer.parseInt(bonus.getText());
                        break;
                }
            }
        }

        transaction = new LinkedHashMap<>();

        transaction.put("name", name);
        transaction.put("cost", cost);

        transaction.put("actions", actions);
        transaction.put("agility", agility);
        transaction.put("combat", combat);
        transaction.put("precision", precision);
        transaction.put("strength", strength);
        transaction.put("tech", tech);
        transaction.put("toughness", toughness);

        return transaction;
    }

}
