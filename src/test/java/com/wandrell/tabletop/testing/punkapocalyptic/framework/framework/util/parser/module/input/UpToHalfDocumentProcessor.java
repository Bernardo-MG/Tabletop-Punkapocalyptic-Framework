package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;
import com.wandrell.util.parser.xml.input.JDOMDocumentInputProcessor;

public final class UpToHalfDocumentProcessor implements
        JDOMDocumentInputProcessor<Collection<Collection<Object>>> {

    private static final Logger logger = LoggerFactory
                                               .getLogger(UpToHalfDocumentProcessor.class);

    private static final Logger getLogger() {
        return logger;
    }

    public UpToHalfDocumentProcessor() {
        super();
    }

    @Override
    public final Collection<Collection<Object>> process(final Document doc) {
        final Collection<Collection<Object>> colData;

        colData = new LinkedHashSet<>();
        for (final Element node : doc.getRootElement().getChildren()) {
            colData.add(readNode(node));
        }

        return colData;
    }

    private final Collection<Object> readNode(final Element node) {
        final Collection<Object> data;
        final Integer units;
        final Integer total;

        units = Integer.parseInt(node.getChild(TestXMLConf.NODE_UNITS)
                .getText());
        total = Integer.parseInt(node.getChild(TestXMLConf.NODE_TOTAL)
                .getText());

        data = new LinkedList<>();
        data.add(units);
        data.add(total);

        getLogger().debug(
                String.format("Read constraint for %d units of a total of %d",
                        units, total));

        return data;
    }

}
