package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
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
        Collection<SpecialRule> rules;
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
            rules = getRules(node.getChild(ModelNodeConf.RULES));

            weapon = new DefaultMeleeWeapon(name, cost, strength, penetration,
                    combat, rules);

            weapons.put(name, weapon);
        }

        return weapons;
    }

    private final Collection<SpecialRule> getRules(final Element rulesNode) {
        Collection<SpecialRule> rules;

        rules = new LinkedList<>();
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(getSpecialRuleDAO().getSpecialRule(rule.getText()));
            }
        }

        return rules;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
