package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;
import com.wandrell.util.parser.module.input.FilteredJDOMInputParserProcessor.FilteredNodesProcessor;

public final class UnitLimitFilteredNodesProcessor implements
        FilteredNodesProcessor<Collection<Collection<Object>>> {

    private static final Logger logger = LoggerFactory
                                               .getLogger(UnitLimitFilteredNodesProcessor.class);

    private static final Logger getLogger() {
        return logger;
    }

    public UnitLimitFilteredNodesProcessor() {
        super();
    }

    @Override
    public final Collection<Collection<Object>> readNodes(
            final Collection<Element> nodes) {
        final Collection<Collection<Object>> colData;

        colData = new LinkedHashSet<>();
        for (final Element node : nodes) {
            colData.add(readNode(node));
        }

        return colData;
    }

    private final Collection<Object> readNode(final Element node) {
        final Collection<Object> data;
        final Integer units;
        final Integer limit;

        units = Integer.parseInt(node.getChild(TestXMLConf.NODE_UNITS)
                .getText());
        limit = Integer.parseInt(node.getChild(TestXMLConf.NODE_LIMIT)
                .getText());

        data = new LinkedList<>();
        data.add(limit);
        data.add(units);

        getLogger().debug(
                String.format("Read constraint for %d units and a limit of %d",
                        units, limit));

        return data;
    }

}
