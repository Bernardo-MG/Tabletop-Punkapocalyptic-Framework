package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseFactionsCommand implements
        ReturnCommand<Map<String, Faction>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseFactionsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

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
            factions.put(name, getModelService().getFaction(name));
        }

        return factions;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final ModelService getModelService() {
        return modelService;
    }

}
