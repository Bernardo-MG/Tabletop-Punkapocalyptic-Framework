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

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitsCommand implements
        ReturnCommand<Map<String, Unit>>, ModelServiceAware {

    private final Document                 document;
    private ModelService                   modelService;
    private final Map<String, SpecialRule> rules;

    public ParseUnitsCommand(final Document doc,
            final Map<String, SpecialRule> rules) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(rules, "Received a null pointer as rules");

        document = doc;
        this.rules = rules;
    }

    @Override
    public final Map<String, Unit> execute() throws Exception {
        final Map<String, Unit> units;
        final Map<String, Collection<SpecialRule>> rules;
        final Collection<Element> nodes;
        Unit unit;

        rules = getUnitRules();

        nodes = XPathFactory.instance()
                .compile("//unit_profiles/unit_profile", Filters.element())
                .evaluate(getDocument());

        units = new LinkedHashMap<>();
        for (final Element node : nodes) {
            unit = parseNode(node, rules);
            units.put(unit.getUnitName(), unit);
        }

        return units;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final ModelService getModelService() {
        return modelService;
    }

    private final Map<String, SpecialRule> getSpecialRules() {
        return rules;
    }

    private final Map<String, Collection<SpecialRule>> getUnitRules() {
        final Map<String, Collection<SpecialRule>> result;
        final Collection<Element> nodes;
        Collection<SpecialRule> rules;

        nodes = XPathFactory.instance()
                .compile("//unit_rules/unit_rule", Filters.element())
                .evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            rules = new LinkedList<>();
            for (final Element rule : node.getChild("rules").getChildren()) {
                rules.add(getSpecialRules().get(rule.getText()));
            }

            result.put(node.getChild("unit").getText(), rules);
        }

        return result;
    }

    private final Unit parseNode(final Element node,
            final Map<String, Collection<SpecialRule>> rules) {
        final String name;
        final Integer actions;
        final Integer combat;
        final Integer precision;
        final Integer agility;
        final Integer strength;
        final Integer toughness;
        final Integer tech;
        final Unit unit;
        final Integer cost;
        final Collection<SpecialRule> unitRules;

        name = node.getChildText(ModelNodeConf.NAME);

        actions = Integer.parseInt(node.getChildText(ModelNodeConf.ACTIONS));
        combat = Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT));
        precision = Integer
                .parseInt(node.getChildText(ModelNodeConf.PRECISION));
        agility = Integer.parseInt(node.getChildText(ModelNodeConf.AGILITY));
        strength = Integer.parseInt(node.getChildText(ModelNodeConf.STRENGTH));
        toughness = Integer
                .parseInt(node.getChildText(ModelNodeConf.TOUGHNESS));
        tech = Integer.parseInt(node.getChildText(ModelNodeConf.TECH));

        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        if (rules.containsKey(name)) {
            unitRules = rules.get(name);
        } else {
            unitRules = new LinkedList<>();
        }

        unit = getModelService().getUnit(name, actions, agility, combat,
                precision, strength, tech, toughness, cost, unitRules);

        return unit;
    }

}
