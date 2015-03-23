package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;

public final class UnitMutationsTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public UnitMutationsTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final String unit;
        final Integer max;
        final Element whiteList;
        final Element initialNode;
        final Collection<String> initial;
        final Collection<String> allowed;

        unit = input.getChildText("unit");

        max = Integer.parseInt(input.getChildText(ModelNodeConf.MAX_MUTATIONS));

        allowed = new LinkedHashSet<>();
        whiteList = input.getChild(ModelNodeConf.WHITE_LIST);
        if (whiteList != null) {
            for (final Element nodeAllowed : whiteList.getChildren()) {
                allowed.add(nodeAllowed.getText());
            }
        }

        initial = new LinkedHashSet<>();
        initialNode = input.getChild("initial");
        if (initialNode != null) {
            for (final Element nodeAllowed : initialNode.getChildren()) {
                initial.add(nodeAllowed.getText());
            }
        }

        transaction = new LinkedHashMap<>();
        transaction.put("unit", unit);
        transaction.put("max_mutations", max);
        transaction.put("white_list", allowed);
        transaction.put("initial", initial);

        return transaction;
    }

}
