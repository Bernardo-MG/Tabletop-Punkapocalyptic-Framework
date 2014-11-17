package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.util.command.ReturnCommand;

public final class ParseWeaponsRulesCommand implements
        ReturnCommand<Map<String, MeleeWeapon>> {

    private final Document                 document;
    private final Map<String, SpecialRule> rules;
    private final Map<String, Weapon>      weapons;

    public ParseWeaponsRulesCommand(final Document doc,
            final Map<String, Weapon> weapons,
            final Map<String, SpecialRule> rules) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(weapons, "Received a null pointer as weapons");
        checkNotNull(rules, "Received a null pointer as rules");

        document = doc;
        this.weapons = weapons;
        this.rules = rules;
    }

    @Override
    public final Map<String, MeleeWeapon> execute() throws Exception {
        final Map<String, MeleeWeapon> weapons;
        final Collection<Element> nodes;
        Collection<SpecialRule> rules;
        Weapon weapon;

        nodes = XPathFactory
                .instance()
                .compile(
                        "//weapon_melee_profile[rules]|//weapon_ranged_profile[rules]",
                        Filters.element()).evaluate(getDocument());

        weapons = new LinkedHashMap<>();
        for (final Element node : nodes) {
            weapon = getWeapons().get(node.getChildText("name"));
            rules = new LinkedList<>();
            for (final Element rule : node.getChild("rules").getChildren()) {
                rules.add(getRules().get(rule.getText()));
            }
            weapon.setRules(rules);
        }

        return weapons;
    }

    private final Document getDocument() {
        return document;
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
