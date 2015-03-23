package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;

public final class FactionTransactionParser implements
        Parser<Element, Map<String, Object>> {

    public FactionTransactionParser() {
        super();
    }

    @Override
    public final Map<String, Object> parse(final Element input) {
        final Map<String, Object> transaction;

        transaction = new LinkedHashMap<>();

        transaction.put("name", input.getChildText("name"));

        return transaction;
    }

}
