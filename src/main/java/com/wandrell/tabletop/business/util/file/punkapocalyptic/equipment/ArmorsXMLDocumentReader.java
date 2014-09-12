package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultArmor;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class ArmorsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Armor>> {

    private final RulesetDAO daoRule;

    public ArmorsXMLDocumentReader(final RulesetDAO dao) {
        super();

        daoRule = dao;
    }

    @Override
    public final Map<String, Armor> getValue(final Document doc) {
        final Element root;
        final Map<String, Armor> armors;
        String name;
        Integer protection;
        Collection<SpecialRule> rules;
        Armor armor;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);
            protection = Integer.parseInt(node
                    .getChildText(ModelNodeConf.PROTECTION));
            rules = getRules(node.getChild(ModelNodeConf.RULES));

            armor = new DefaultArmor(name, protection, rules);

            armors.put(name, armor);
        }

        return armors;
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
