package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.punkapocalyptic.rule.DefaultSpecialRule;
import com.wandrell.tabletop.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class SpecialRulesXMLDocumentReader implements
        XMLDocumentReader<Map<String, SpecialRule>> {

    public SpecialRulesXMLDocumentReader() {
        super();
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

}
