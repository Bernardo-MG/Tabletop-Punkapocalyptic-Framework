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
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;

public final class ArmorsXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Armor>> {

    private Document                       doc;
    private final Map<String, SpecialRule> rules;

    public ArmorsXMLDocumentReader(final Map<String, SpecialRule> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Armor> getValue() {
        final Element root;
        final Map<String, Armor> armors;
        String name;
        Integer protection;
        Collection<SpecialRule> rules;
        Armor armor;

        root = getDocument().getRootElement();

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

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

    private final Collection<SpecialRule> getRules(final Element rulesNode) {
        Collection<SpecialRule> rules;

        rules = new LinkedList<>();
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(getRules().get(rule.getText()));
            }
        }

        return rules;
    }

}
