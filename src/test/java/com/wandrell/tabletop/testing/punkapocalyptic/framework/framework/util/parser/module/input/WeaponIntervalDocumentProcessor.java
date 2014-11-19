package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.module.input;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.TestXMLConf;
import com.wandrell.util.parser.module.input.JDOMDocumentProcessor;

public final class WeaponIntervalDocumentProcessor implements
        JDOMDocumentProcessor<Collection<Collection<Object>>> {

    private static final Logger logger = LoggerFactory
                                               .getLogger(UpToHalfDocumentProcessor.class);

    private static final Logger getLogger() {
        return logger;
    }

    public WeaponIntervalDocumentProcessor() {
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
        final Integer weapons;
        final Integer min;
        final Integer max;

        weapons = Integer.parseInt(node.getChild(TestXMLConf.NODE_WEAPONS)
                .getText());
        min = Integer.parseInt(node.getChild(TestXMLConf.NODE_MIN).getText());
        max = Integer.parseInt(node.getChild(TestXMLConf.NODE_MAX).getText());

        data = new LinkedList<>();
        data.add(weapons);
        data.add(min);
        data.add(max);

        getLogger().debug(
                String.format(
                        "Read constraint for %d weapons in interval [%d,%d]",
                        weapons, min, max));

        return data;
    }

}
