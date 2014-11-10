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
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitsCommand implements
        ReturnCommand<Map<String, Unit>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseUnitsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, Unit> execute() throws Exception {
        final Map<String, Unit> units;
        final Collection<Element> nodes;
        Unit unit;

        nodes = XPathFactory.instance()
                .compile("//unit_profiles/unit_profile", Filters.element())
                .evaluate(getDocument());

        units = new LinkedHashMap<>();
        for (final Element node : nodes) {
            unit = parseNode(node);
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

    private final Unit parseNode(final Element node) {
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

        unit = getModelService().getUnit(name, actions, agility, combat,
                precision, strength, tech, toughness, cost,
                new LinkedList<SpecialRule>());

        return unit;
    }

}
