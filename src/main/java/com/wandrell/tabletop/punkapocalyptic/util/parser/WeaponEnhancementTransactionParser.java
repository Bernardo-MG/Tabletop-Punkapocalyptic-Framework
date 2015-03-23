package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class WeaponEnhancementTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public WeaponEnhancementTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final String name;
        final Integer cost;

        name = input.getChildText(ModelNodeConf.NAME);
        cost = Integer.parseInt(input.getChildText(ModelNodeConf.COST));

        transaction = new LinkedHashMap<>();

        transaction.put("name", name);
        transaction.put("cost", cost);

        return transaction;
    }

}
