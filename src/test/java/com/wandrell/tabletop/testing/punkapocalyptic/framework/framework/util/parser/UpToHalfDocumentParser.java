package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;

public final class UpToHalfDocumentParser implements
        Parser<Document, Collection<Collection<Object>>> {

    private static final Logger logger = LoggerFactory
                                               .getLogger(UpToHalfDocumentParser.class);

    private static final Logger getLogger() {
        return logger;
    }

    public UpToHalfDocumentParser() {
        super();
    }

    @Override
    public final Collection<Collection<Object>> parse(final Document doc) {
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
        total = Integer.parseInt(node.getChild(TestXMLConf.NODE_GANG_SIZE)
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
