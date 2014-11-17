package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jdom2.Element;

import com.wandrell.util.parser.module.input.FilteredJDOMInputParserProcessor.FilteredNodesProcessor;

public final class MaxAllowedUnitsFilteredNodesProcessor implements
        FilteredNodesProcessor<Set<Collection<Integer>>> {

    public MaxAllowedUnitsFilteredNodesProcessor() {
        super();
    }

    @Override
    public final Set<Collection<Integer>> readNodes(
            final Collection<Element> nodes) {
        final Set<Collection<Integer>> data;
        Collection<Integer> pair;

        data = new LinkedHashSet<>();
        for (final Element node : nodes) {
            pair = new LinkedList<>();
            pair.add(Integer.parseInt(node.getChildText("points")));
            pair.add(Integer.parseInt(node.getChildText("units")));

            data.add(pair);
        }

        return data;
    }

}
