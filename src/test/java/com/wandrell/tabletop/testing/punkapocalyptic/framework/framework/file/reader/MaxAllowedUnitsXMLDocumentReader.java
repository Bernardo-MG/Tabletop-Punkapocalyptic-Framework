package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.file.reader;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class MaxAllowedUnitsXMLDocumentReader implements
        XMLDocumentReader<Collection<Collection<Integer>>> {

    public MaxAllowedUnitsXMLDocumentReader() {
        super();
    }

    @Override
    public final Collection<Collection<Integer>> getValue(final Document doc) {
        final Element root;
        final Collection<Collection<Integer>> data;
        Collection<Integer> pair;

        root = doc.getRootElement();

        data = new LinkedList<>();
        for (final Element node : root.getChildren()) {
            pair = new LinkedList<>();
            pair.add(Integer.parseInt(node.getChildText("points")));
            pair.add(Integer.parseInt(node.getChildText("units")));

            data.add(pair);
        }

        return data;
    }

}
