package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;

public final class MaxAllowedUnitsDocumentParser implements
        Parser<Document, Collection<Collection<Object>>> {

    public MaxAllowedUnitsDocumentParser() {
        super();
    }

    @Override
    public final Collection<Collection<Object>> parse(final Document doc) {
        final Collection<Collection<Object>> data;
        Collection<Object> pair;

        data = new LinkedHashSet<>();
        for (final Element node : doc.getRootElement().getChildren()) {
            pair = new LinkedList<>();
            pair.add(Integer.parseInt(node
                    .getChildText(TestXMLConf.NODE_POINTS)));
            pair.add(Integer.parseInt(node.getChildText(TestXMLConf.NODE_UNITS)));

            data.add(pair);
        }

        return data;
    }

}
