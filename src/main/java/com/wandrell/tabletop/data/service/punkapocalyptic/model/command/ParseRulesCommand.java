package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.SpecialRuleNameConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.DefaultSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.FirearmSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.TwoHandedSpecialRule;
import com.wandrell.util.command.ReturnCommand;

public final class ParseRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>> {

    private final Document    document;
    private final MeleeWeapon weaponDefault;

    public ParseRulesCommand(final Document doc, final MeleeWeapon weapon) {
        super();

        document = doc;
        weaponDefault = weapon;
    }

    @Override
    public final Map<String, SpecialRule> execute() throws Exception {
        final Map<String, SpecialRule> rules;
        final Map<String, SpecialRule> result;
        final Collection<Element> nodes;
        SpecialRule rule;

        rules = new LinkedHashMap<>();

        // TODO : Use a Spring context or something
        rules.put(SpecialRuleNameConf.TWO_HANDED, new TwoHandedSpecialRule(
                SpecialRuleNameConf.TWO_HANDED, getDefaultWeapon()));

        rules.put("automatic", new DefaultSpecialRule("automatic"));
        rules.put("cumbersome", new DefaultSpecialRule("cumbersome"));
        rules.put("dead_slow", new DefaultSpecialRule("dead_slow"));
        rules.put("firearm", new FirearmSpecialRule("firearm"));
        rules.put("hard_to_use", new DefaultSpecialRule("hard_to_use"));
        rules.put("pellets", new DefaultSpecialRule("pellets"));

        nodes = XPathFactory.instance().compile("//rule", Filters.element())
                .evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            rule = rules.get(node.getText());

            result.put(rule.getName(), rule);
        }

        return result;
    }

    private final MeleeWeapon getDefaultWeapon() {
        return weaponDefault;
    }

    private final Document getDocument() {
        return document;
    }

}
