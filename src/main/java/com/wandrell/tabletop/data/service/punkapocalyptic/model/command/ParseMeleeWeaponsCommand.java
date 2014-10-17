package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.util.command.ReturnCommand;

public final class ParseMeleeWeaponsCommand implements
        ReturnCommand<Map<String, MeleeWeapon>> {

    private final Document document;

    public ParseMeleeWeaponsCommand(final Document doc) {
        super();

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

    private final Document getDocument() {
        return document;
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

        weapon = new DefaultMeleeWeapon(name, cost, strength, penetration,
                combat);

        return weapon;
    }

}
