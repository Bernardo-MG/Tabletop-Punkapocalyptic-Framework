package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class MeleeWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Weapon>> {

    private final RulesetDAO daoRule;

    public MeleeWeaponsXMLDocumentReader(final RulesetDAO dao) {
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
        Integer hands;
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
            hands = Integer.parseInt(node.getChildText("hands"));

            rules = new LinkedList<>();

            rulesNode = node.getChild("rules");
            if (rulesNode != null) {
                for (final Element rule : rulesNode.getChildren()) {
                    rules.add(getSpecialRuleDAO()
                            .getSpecialRule(rule.getText()));
                }
            }

            weapon = new DefaultMeleeWeapon(name, cost, hands, strength,
                    penetration, combat, rules);

            weapons.put(name, weapon);
        }

        return weapons;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
