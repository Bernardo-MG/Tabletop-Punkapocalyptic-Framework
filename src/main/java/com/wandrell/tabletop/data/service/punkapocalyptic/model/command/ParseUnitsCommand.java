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
import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.EditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic.UnitValorationStore;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitsCommand implements
        ReturnCommand<Map<String, Unit>>, RulesetServiceAware {

    private final Document document;
    private RulesetService serviceRuleset;

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

    private final Document getDocument() {
        return document;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Unit parseNode(final Element node) {
        final PunkapocalypticFactory factory;
        final String name;
        final EditableValueHandler actions;
        final EditableValueHandler combat;
        final EditableValueHandler precision;
        final EditableValueHandler agility;
        final EditableValueHandler strength;
        final EditableValueHandler toughness;
        final EditableValueHandler tech;
        final ValueHandler valoration;
        final UnitValorationStore store;
        final Unit unit;
        final Integer cost;

        factory = PunkapocalypticFactory.getInstance();

        name = node.getChildText(ModelNodeConf.NAME);

        actions = factory.getAttribute(ModelNodeConf.ACTIONS,
                Integer.parseInt(node.getChildText(ModelNodeConf.ACTIONS)));
        combat = factory.getAttribute(ModelNodeConf.COMBAT,
                Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT)));
        precision = factory.getAttribute(ModelNodeConf.PRECISION,
                Integer.parseInt(node.getChildText(ModelNodeConf.PRECISION)));
        agility = factory.getAttribute(ModelNodeConf.AGILITY,
                Integer.parseInt(node.getChildText(ModelNodeConf.AGILITY)));
        strength = factory.getAttribute(ModelNodeConf.STRENGTH,
                Integer.parseInt(node.getChildText(ModelNodeConf.STRENGTH)));
        toughness = factory.getAttribute(ModelNodeConf.TOUGHNESS,
                Integer.parseInt(node.getChildText(ModelNodeConf.TOUGHNESS)));
        tech = factory.getAttribute(ModelNodeConf.TECH,
                Integer.parseInt(node.getChildText(ModelNodeConf.TECH)));

        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        store = new UnitValorationStore(getRulesetService());
        valoration = new ModularDerivedValueHandler("valoration", store);

        unit = new DefaultUnit(name, actions, agility, combat, precision,
                strength, tech, toughness, cost, new LinkedList<SpecialRule>(),
                valoration);

        store.setUnit(unit);

        return unit;
    }

    @Override
    public final void setRulesetService(final RulesetService service) {
        serviceRuleset = service;
    }

}
