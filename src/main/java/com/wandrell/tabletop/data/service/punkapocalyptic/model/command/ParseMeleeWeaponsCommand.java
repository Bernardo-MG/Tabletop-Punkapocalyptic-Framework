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
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseMeleeWeaponsCommand implements
        ReturnCommand<Map<String, MeleeWeapon>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseMeleeWeaponsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, MeleeWeapon> execute() throws Exception {
        final Map<String, MeleeWeapon> weapons;
        final Collection<Element> nodes;
        MeleeWeapon weapon;

        nodes = XPathFactory.instance()
                .compile("//weapon_melee_profile", Filters.element())
                .evaluate(getDocument());

        weapons = new LinkedHashMap<>();
        for (final Element node : nodes) {
            weapon = parseNode(node);
            weapons.put(weapon.getName(), weapon);
        }

        return weapons;
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

    private final MeleeWeapon parseNode(final Element node) {
        String name;
        Integer strength;
        Integer penetration;
        Integer combat;
        Integer cost;
        MeleeWeapon weapon;

        name = node.getChildText(ModelNodeConf.NAME);
        strength = Integer.parseInt(node.getChildText(ModelNodeConf.STRENGTH));
        penetration = Integer.parseInt(node
                .getChildText(ModelNodeConf.PENETRATION));
        combat = Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT));
        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        weapon = getModelService().getMeleeWeapon(name, cost, strength,
                penetration, combat);

        return weapon;
    }

}
