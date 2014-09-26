package com.wandrell.tabletop.business.util.file.punkapocalyptic.faction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;

public final class FactionUnitsXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Collection<AvailabilityUnit>>> {

    private Document                            doc;
    private final Map<String, AvailabilityUnit> units;

    public FactionUnitsXMLDocumentReader(
            final Map<String, AvailabilityUnit> units) {
        super();

        this.units = units;
    }

    @Override
    public final Map<String, Collection<AvailabilityUnit>> getValue() {
        final Map<String, Collection<AvailabilityUnit>> factionUnits;
        final Element root;
        Collection<AvailabilityUnit> units;

        factionUnits = new LinkedHashMap<>();
        root = getDocument().getRootElement();

        for (final Element node : root.getChildren()) {
            units = new LinkedList<>();

            for (final Element unit : node.getChild(ModelNodeConf.UNITS)
                    .getChildren()) {
                units.add(getUnits().get(unit.getChildText(ModelNodeConf.NAME)));
            }

            factionUnits.put(node.getChildText(ModelNodeConf.FACTION), units);
        }

        return factionUnits;
    }

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, AvailabilityUnit> getUnits() {
        return units;
    }

}
