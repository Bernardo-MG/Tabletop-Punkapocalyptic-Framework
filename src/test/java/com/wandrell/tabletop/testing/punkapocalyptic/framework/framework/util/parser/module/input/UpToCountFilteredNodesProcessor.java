package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;
import com.wandrell.util.parser.module.input.FilteredJDOMInputParserProcessor.FilteredNodesProcessor;

public final class UpToCountFilteredNodesProcessor implements
        FilteredNodesProcessor<Collection<Collection<Object>>> {

    private static final Logger logger = LoggerFactory
                                               .getLogger(UpToCountFilteredNodesProcessor.class);

    private static final Logger getLogger() {
        return logger;
    }

    public UpToCountFilteredNodesProcessor() {
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
        final String unit;
        final Integer count;
        final List<String> units;
        final Element list;

        unit = node.getChild(TestXMLConf.NODE_UNIT).getText();
        count = Integer.parseInt(node.getChild(TestXMLConf.NODE_COUNT)
                .getText());

        list = node.getChild(TestXMLConf.NODE_LIST);
        units = new LinkedList<>();
        for (final Element u : list.getChildren()) {
            units.add(u.getText());
        }

        data = new LinkedList<>();
        data.add(unit);
        data.add(count);
        data.add(units);

        getLogger()
                .debug(String
                        .format("Read constraint for unit '%s' and count %d applied to list %s",
                                unit, count, units));

        return data;
    }

}
