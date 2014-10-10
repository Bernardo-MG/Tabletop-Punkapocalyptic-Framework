package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class RangedWeaponsRulesParserInterpreter implements
        ParserInterpreter<Map<String, Collection<SpecialRule>>, Document> {

    private final Map<String, SpecialRule> rules;

    public RangedWeaponsRulesParserInterpreter(
            final Map<String, SpecialRule> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Collection<SpecialRule>> parse(final Document doc) {
        final Element root;
        final Map<String, Collection<SpecialRule>> result;
        Collection<SpecialRule> rules;

        root = doc.getRootElement();

        result = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            rules = getRules(node.getChild(ModelNodeConf.RULES));

            result.put(node.getChildText(ModelNodeConf.NAME), rules);
        }

        return result;
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
