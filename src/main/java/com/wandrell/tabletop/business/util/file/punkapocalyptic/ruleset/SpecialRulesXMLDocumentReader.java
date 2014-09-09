package com.wandrell.tabletop.business.util.file.punkapocalyptic.ruleset;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.DefaultSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class SpecialRulesXMLDocumentReader implements
        XMLDocumentReader<Map<String, SpecialRule>> {

    private final RulesetDAO daoRule;

    public SpecialRulesXMLDocumentReader(final RulesetDAO dao) {
        super();

        daoRule = dao;
    }

    @Override
    public final Map<String, SpecialRule> getValue(final Document doc) {
        final Element root;
        final Map<String, SpecialRule> rules;
        String name;
        SpecialRule rule;

        root = doc.getRootElement();

        rules = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText("name");

            rule = new DefaultSpecialRule(name);

            rules.put(name, rule);
        }

        return rules;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
