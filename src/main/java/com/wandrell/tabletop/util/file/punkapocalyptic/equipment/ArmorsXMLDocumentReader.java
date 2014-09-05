package com.wandrell.tabletop.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.data.dao.punkapocalyptic.SpecialRuleDAO;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.DefaultArmor;
import com.wandrell.tabletop.model.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class ArmorsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Armor>> {

    private final SpecialRuleDAO daoRule;

    public ArmorsXMLDocumentReader(final SpecialRuleDAO dao) {
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
        Element rulesNode;
        Armor armor;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText("name");
            protection = Integer.parseInt(node.getChildText("protection"));

            rules = new LinkedList<>();
            rulesNode = node.getChild("rules");
            if (rulesNode != null) {
                for (final Element rule : rulesNode.getChildren()) {
                    rules.add(getSpecialRuleDAO()
                            .getSpecialRule(rule.getText()));
                }
            }

            armor = new DefaultArmor(name, protection, rules);

            armors.put(name, armor);
        }

        return armors;
    }

    protected final SpecialRuleDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
