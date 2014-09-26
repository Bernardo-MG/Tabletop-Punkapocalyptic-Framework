package com.wandrell.tabletop.business.util.file.punkapocalyptic.faction;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;

public final class FactionNameXMLDocumentReader implements
        JDOMXMLInterpreter<Collection<String>> {

    private Document doc;

    public FactionNameXMLDocumentReader() {
        super();
    }

    @Override
    public final Collection<String> getValue() {
        final Collection<String> names;
        final Element root;

        names = new LinkedList<>();
        root = getDocument().getRootElement();

        for (final Element node : root.getChildren()) {
            names.add(node.getChildText(ModelNodeConf.NAME));
        }

        return names;
    }

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Document getDocument() {
        return doc;
    }

}
