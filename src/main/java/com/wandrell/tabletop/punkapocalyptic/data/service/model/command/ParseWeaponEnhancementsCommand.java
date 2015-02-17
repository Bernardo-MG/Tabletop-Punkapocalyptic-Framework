package com.wandrell.tabletop.punkapocalyptic.data.service.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseWeaponEnhancementsCommand implements
        ReturnCommand<Collection<WeaponEnhancement>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseWeaponEnhancementsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Collection<WeaponEnhancement> execute() throws Exception {
        final Collection<WeaponEnhancement> enhancements;
        final Collection<Element> nodes;

        nodes = XPathFactory.instance()
                .compile("//weapon_enhancement_profile", Filters.element())
                .evaluate(getDocument());

        enhancements = new LinkedList<>();
        for (final Element node : nodes) {
            enhancements.add(parseNode(node));
        }

        return enhancements;
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

    private final WeaponEnhancement parseNode(final Element node) {
        final WeaponEnhancement enhancement;
        final String name;
        final Integer cost;

        name = node.getChildText(ModelNodeConf.NAME);
        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        enhancement = getModelService().getWeaponEnhancement(name, cost);

        return enhancement;
    }

}
