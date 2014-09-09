package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class UnitConstraintsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<String>>> {

    public UnitConstraintsXMLDocumentReader() {
        super();
    }

    @Override
    public final Map<String, Collection<String>> getValue(final Document doc) {
        final Element root;
        final Map<String, Collection<String>> constraints;
        Element nodeConstraints;
        Collection<String> consts;

        root = doc.getRootElement();

        constraints = new LinkedHashMap<>();
        for (final Element nodeFaction : root.getChildren()) {
            for (final Element nodeUnit : nodeFaction.getChild("units")
                    .getChildren()) {
                consts = new LinkedList<>();
                nodeConstraints = nodeUnit.getChild("constraints");
                if (nodeConstraints != null) {
                    for (final Element constraintNode : nodeConstraints
                            .getChildren()) {
                        consts.add(constraintNode.getText());
                    }
                }

                constraints.put(nodeUnit.getChildText("name"), consts);
            }
        }

        return constraints;
    }

}
