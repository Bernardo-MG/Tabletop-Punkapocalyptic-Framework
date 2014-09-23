package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;

public final class MeleeWeaponsRulesXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Collection<SpecialRule>>> {

    private Document                       doc;
    private final Map<String, SpecialRule> rules;

    public MeleeWeaponsRulesXMLDocumentReader(
            final Map<String, SpecialRule> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Collection<SpecialRule>> getValue() {
        final Element root;
        final Map<String, Collection<SpecialRule>> result;
        Collection<SpecialRule> rules;

        root = getDocument().getRootElement();

        result = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            rules = getRules(node.getChild(ModelNodeConf.RULES));

            result.put(node.getChildText(ModelNodeConf.NAME), rules);
        }

        return result;
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
