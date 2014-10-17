package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.DefaultFaction;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.util.command.ReturnCommand;

public final class ParseFactionsCommand implements
        ReturnCommand<Map<String, Faction>> {

    private final Document document;

    public ParseFactionsCommand(final Document doc) {
        super();

        document = doc;
    }

    @Override
    public final Map<String, Faction> execute() throws Exception {
        final Map<String, Faction> factions;
        final Collection<Element> nodes;
        String name;

        nodes = XPathFactory.instance()
                .compile("//faction_profile", Filters.element())
                .evaluate(getDocument());

        factions = new LinkedHashMap<>();
        for (final Element node : nodes) {
            name = node.getChildText("name");
            factions.put(name, new DefaultFaction(name));
        }

        return factions;
    }

    private final Document getDocument() {
        return document;
    }

}
