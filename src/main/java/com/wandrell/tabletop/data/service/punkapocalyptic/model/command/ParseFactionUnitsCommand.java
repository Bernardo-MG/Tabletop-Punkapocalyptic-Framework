package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseFactionUnitsCommand implements
        ReturnCommand<Map<String, Collection<AvailabilityUnit>>> {

    private final Document                      document;
    private final Map<String, AvailabilityUnit> unitAvailabilities;

    public ParseFactionUnitsCommand(final Document doc,
            final Map<String, AvailabilityUnit> unitAvailabilities) {
        super();

        document = doc;
        this.unitAvailabilities = unitAvailabilities;
    }

    @Override
    public final Map<String, Collection<AvailabilityUnit>> execute()
            throws Exception {
        final Map<String, Collection<AvailabilityUnit>> factions;
        final Collection<Element> nodes;
        Collection<AvailabilityUnit> units;
        String faction;

        nodes = XPathFactory.instance()
                .compile("//faction_unit", Filters.element())
                .evaluate(getDocument());

        factions = new LinkedHashMap<>();
        for (final Element node : nodes) {
            faction = node.getChildText("faction");
            units = new LinkedList<>();
            for (final Element unit : node.getChild("units").getChildren()) {
                units.add(getAvailabilities().get(unit.getChildText("name")));
            }

            factions.put(faction, units);
        }

        return factions;
    }

    private final Map<String, AvailabilityUnit> getAvailabilities() {
        return unitAvailabilities;
    }

    private final Document getDocument() {
        return document;
    }

}
