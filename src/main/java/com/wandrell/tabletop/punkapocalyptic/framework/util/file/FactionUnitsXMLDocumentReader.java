package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class FactionUnitsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<String>>> {

    public FactionUnitsXMLDocumentReader() {
        super();
    }

    @Override
    public final Map<String, Collection<String>> getValue(final Document doc) {
        final Map<String, Collection<String>> factionUnits;
        final Element root;
        Collection<String> units;

        factionUnits = new LinkedHashMap<>();
        root = doc.getRootElement();

        for (final Element node : root.getChildren()) {
            units = new LinkedList<>();

            for (final Element unit : node.getChild("units").getChildren()) {
                units.add(unit.getChildText("unit"));
            }

            factionUnits.put(node.getChildText("name"), units);
        }

        return factionUnits;
    }

}
