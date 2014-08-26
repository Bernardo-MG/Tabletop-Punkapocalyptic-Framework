package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class FactionNameXMLDocumentReader implements
        XMLDocumentReader<Collection<String>> {

    public FactionNameXMLDocumentReader() {
        super();
    }

    @Override
    public final Collection<String> getValue(final Document doc) {
        final Collection<String> names;
        final Element root;

        names = new LinkedList<>();
        root = doc.getRootElement();

        for (final Element node : root.getChildren()) {
            names.add(node.getAttributeValue("name"));
        }

        return names;
    }

}
