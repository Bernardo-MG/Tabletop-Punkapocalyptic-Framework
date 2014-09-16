package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.file.reader;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Element;

import com.wandrell.util.file.xml.module.reader.DefaultFilteredXMLDocumentReader.FilteredNodesReader;

public final class MaxAllowedUnitsNodesReader implements
        FilteredNodesReader<Collection<Collection<Integer>>> {

    public MaxAllowedUnitsNodesReader() {
        super();
    }

    @Override
    public final Collection<Collection<Integer>> readNodes(
            final Collection<Element> nodes) {
        final Collection<Collection<Integer>> data;
        Collection<Integer> pair;

        data = new LinkedList<>();
        for (final Element node : nodes) {
            pair = new LinkedList<>();
            pair.add(Integer.parseInt(node.getChildText("points")));
            pair.add(Integer.parseInt(node.getChildText("units")));

            data.add(pair);
        }

        return data;
    }

}
