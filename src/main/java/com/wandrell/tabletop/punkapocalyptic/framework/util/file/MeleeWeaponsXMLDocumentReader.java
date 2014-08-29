package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.punkapocalyptic.framework.api.dao.SpecialRuleDAO;
import com.wandrell.tabletop.punkapocalyptic.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class MeleeWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Weapon>> {

    private final SpecialRuleDAO daoRule;

    public MeleeWeaponsXMLDocumentReader(final SpecialRuleDAO dao) {
        super();

        daoRule = dao;
    }

    @Override
    public final Map<String, Weapon> getValue(final Document doc) {
        final Element root;
        final Map<String, Weapon> weapons;
        String name;
        Integer strength;
        Integer penetration;
        Integer combat;
        Integer cost;
        Collection<SpecialRule> rules;
        Weapon weapon;
        Element rulesNode;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText("name");
            strength = Integer.parseInt(node.getChildText("strength"));
            penetration = Integer.parseInt(node.getChildText("penetration"));
            combat = Integer.parseInt(node.getChildText("combat"));
            cost = Integer.parseInt(node.getChildText("cost"));

            rules = new LinkedList<>();

            rulesNode = node.getChild("rules");
            if (rulesNode != null) {
                for (final Element rule : rulesNode.getChildren()) {
                    rules.add(getSpecialRuleDAO()
                            .getSpecialRule(rule.getText()));
                }
            }

            weapon = new DefaultMeleeWeapon(name, cost, strength, penetration,
                    combat, rules);

            weapons.put(name, weapon);
        }

        return weapons;
    }

    protected final SpecialRuleDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
