package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.util.command.ReturnCommand;

public final class ParseWeaponEnhancementsCommand implements
        ReturnCommand<Map<String, WeaponEnhancement>> {

    private final Document document;

    public ParseWeaponEnhancementsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, WeaponEnhancement> execute() throws Exception {
        final Map<String, WeaponEnhancement> enhancements;
        final Collection<Element> nodes;
        WeaponEnhancement enhancement;

        nodes = XPathFactory.instance()
                .compile("//weapon_enhancement_profile", Filters.element())
                .evaluate(getDocument());

        enhancements = new LinkedHashMap<>();
        for (final Element node : nodes) {
            enhancement = parseNode(node);
            enhancements.put(enhancement.getName(), enhancement);
        }

        return enhancements;
    }

    private final Document getDocument() {
        return document;
    }

    private final WeaponEnhancement parseNode(final Element node) {
        final WeaponEnhancement enhancement;
        final String name;
        final Integer cost;

        name = node.getChildText(ModelNodeConf.NAME);
        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        enhancement = PunkapocalypticFactory.getInstance()
                .getWeaponEnhancement(name, cost);

        return enhancement;
    }

}
