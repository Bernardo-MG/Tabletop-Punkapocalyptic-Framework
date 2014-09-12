package com.wandrell.tabletop.business.util.file.punkapocalyptic.faction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

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

            for (final Element unit : node.getChild(ModelNodeConf.UNITS)
                    .getChildren()) {
                units.add(unit.getChildText(ModelNodeConf.NAME));
            }

            factionUnits.put(node.getChildText(ModelNodeConf.FACTION), units);
        }

        return factionUnits;
    }

}
