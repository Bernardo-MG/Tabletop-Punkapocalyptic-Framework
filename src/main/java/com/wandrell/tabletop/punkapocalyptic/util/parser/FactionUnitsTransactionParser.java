package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;

public final class FactionUnitsTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public FactionUnitsTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;
        final String faction;
        final String unit;
        final Collection<Map<String, Object>> constraints;
        Element constraintsNode;
        Element node;
        Collection<String> tags;
        Map<String, Object> constraint;

        faction = input.getChildText("faction");
        unit = input.getChildText("unit");

        constraintsNode = input.getChild("constraints");
        constraints = new LinkedList<>();
        if (constraintsNode != null) {
            for (final Element constraintNode : constraintsNode.getChildren()) {
                constraint = new LinkedHashMap<>();
                constraint.put("name", constraintNode.getChildText("name"));

                node = constraintNode.getChild("unit");
                if (node != null) {
                    tags = new LinkedList<>();
                    tags.add(node.getText());
                    tags.add(constraintNode.getChildText("count"));

                    constraint.put("tags", tags);
                }

                constraints.add(constraint);
            }
        }

        transaction = new LinkedHashMap<>();
        transaction.put("faction", faction);
        transaction.put("unit", unit);
        transaction.put("constraints", constraints);

        return transaction;
    }

}
