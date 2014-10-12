package com.wandrell.tabletop.business.util.file.punkapocalyptic.faction;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class FactionUnitsParserInterpreter implements
        ParserInterpreter<Map<String, Collection<AvailabilityUnit>>, Document> {

    private final Map<String, AvailabilityUnit> units;

    public FactionUnitsParserInterpreter(
            final Map<String, AvailabilityUnit> units) {
        super();

        checkNotNull(units, "Received a null pointer as units");

        this.units = units;
    }

    @Override
    public final Map<String, Collection<AvailabilityUnit>> parse(
            final Document doc) {
        final Map<String, Collection<AvailabilityUnit>> factionUnits;
        final Element root;
        Collection<AvailabilityUnit> units;

        checkNotNull(doc, "Received a null pointer as document");

        factionUnits = new LinkedHashMap<>();
        root = doc.getRootElement();

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

    private final Map<String, AvailabilityUnit> getUnits() {
        return units;
    }

}
