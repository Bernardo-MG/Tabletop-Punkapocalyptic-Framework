package com.wandrell.tabletop.business.util.file.punkapocalyptic.ruleset;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.SpecialRuleNameConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.DefaultSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.TwoHandedSpecialRule;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class SpecialRulesXMLDocumentReader implements
        XMLDocumentReader<Map<String, SpecialRule>> {

    private final MeleeWeapon melee;

    public SpecialRulesXMLDocumentReader(final MeleeWeapon twoHandedWeapon) {
        super();

        melee = twoHandedWeapon;
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
            name = node.getChildText(ModelNodeConf.NAME);

            switch (name) {
                case SpecialRuleNameConf.TWO_HANDED:
                    rule = new TwoHandedSpecialRule(
                            SpecialRuleNameConf.TWO_HANDED, getDefaultWeapon());
                    break;
                default:
                    rule = new DefaultSpecialRule(name);
            }

            rules.put(name, rule);
        }

        return rules;
    }

    protected final MeleeWeapon getDefaultWeapon() {
        return melee;
    }

}
