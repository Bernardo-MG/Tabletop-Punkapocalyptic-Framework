package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class MeleeWeaponsParserInterpreter implements
        ParserInterpreter<Map<String, Weapon>, Document> {

    public MeleeWeaponsParserInterpreter() {
        super();
    }

    @Override
    public final Map<String, Weapon> parse(final Document doc) {
        final Element root;
        final Map<String, Weapon> weapons;
        String name;
        Integer strength;
        Integer penetration;
        Integer combat;
        Integer cost;
        Weapon weapon;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);
            strength = Integer.parseInt(node
                    .getChildText(ModelNodeConf.STRENGTH));
            penetration = Integer.parseInt(node
                    .getChildText(ModelNodeConf.PENETRATION));
            combat = Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT));
            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            weapon = new DefaultMeleeWeapon(name, cost, strength, penetration,
                    combat);

            weapons.put(name, weapon);
        }

        return weapons;
    }

}
