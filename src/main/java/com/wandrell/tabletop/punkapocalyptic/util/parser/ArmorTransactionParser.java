package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class ArmorTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public ArmorTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final Element rulesNode;
        final String name;
        final Integer protection;
        final Collection<String> rules;

        name = input.getChildText(ModelNodeConf.NAME);
        protection = Integer.parseInt(input
                .getChildText(ModelNodeConf.PROTECTION));

        rules = new LinkedList<>();
        rulesNode = input.getChild("rules");
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(rule.getText());
            }
        }

        transaction = new LinkedHashMap<>();

        transaction.put("name", name);
        transaction.put("protection", protection);
        transaction.put("rules", rules);

        return transaction;
    }

}
