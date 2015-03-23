package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class MeleeWeaponTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public MeleeWeaponTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Element rulesNode;
        final String name;
        final Integer strength;
        final Integer penetration;
        final Integer combat;
        final Integer cost;
        final Collection<String> rules;

        name = input.getChildText(ModelNodeConf.NAME);
        strength = Integer.parseInt(input.getChildText(ModelNodeConf.STRENGTH));
        penetration = Integer.parseInt(input
                .getChildText(ModelNodeConf.PENETRATION));
        combat = Integer.parseInt(input.getChildText(ModelNodeConf.COMBAT));
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
        transaction.put("strength", strength);
        transaction.put("penetration", penetration);
        transaction.put("combat", combat);
        transaction.put("cost", cost);
        transaction.put("rules", rules);

        return transaction;
    }

}
