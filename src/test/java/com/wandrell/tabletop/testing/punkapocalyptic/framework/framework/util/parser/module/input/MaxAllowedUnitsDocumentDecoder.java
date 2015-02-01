package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;
import com.wandrell.util.parser.xml.input.JDOMDocumentDecoder;

public final class MaxAllowedUnitsDocumentDecoder implements
        JDOMDocumentDecoder<Set<Collection<Integer>>> {

    public MaxAllowedUnitsDocumentDecoder() {
        super();
    }

    @Override
    public final Set<Collection<Integer>> decode(final Document doc) {
        final Set<Collection<Integer>> data;
        Collection<Integer> pair;

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
