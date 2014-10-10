package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.DefaultDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.EditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic.UnitValorationStore;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public class UnitsParserInterpreter implements
        ParserInterpreter<Map<String, Unit>, Document> {

    private final RulesetService serviceRuleset;

    public UnitsParserInterpreter(final RulesetService service) {
        super();

        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as ruleset service");
        }

        serviceRuleset = service;
    }

    @Override
    public final Map<String, Unit> parse(final Document doc) {
        final Map<String, Unit> units;
        final Element root;
        final PunkapocalypticFactory factory;
        String name;
        EditableValueHandler actions;
        EditableValueHandler combat;
        EditableValueHandler precision;
        EditableValueHandler agility;
        EditableValueHandler strength;
        EditableValueHandler toughness;
        EditableValueHandler tech;
        ValueHandler valoration;
        UnitValorationStore store;
        Unit unit;
        Integer cost;

        root = doc.getRootElement();

        factory = PunkapocalypticFactory.getInstance();

        units = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);

            actions = factory.getAttribute(ModelNodeConf.ACTIONS,
                    Integer.parseInt(node.getChildText(ModelNodeConf.ACTIONS)));
            combat = factory.getAttribute(ModelNodeConf.COMBAT,
                    Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT)));
            precision = factory.getAttribute(ModelNodeConf.PRECISION, Integer
                    .parseInt(node.getChildText(ModelNodeConf.PRECISION)));
            agility = factory.getAttribute(ModelNodeConf.AGILITY,
                    Integer.parseInt(node.getChildText(ModelNodeConf.AGILITY)));
            strength = factory
                    .getAttribute(ModelNodeConf.STRENGTH, Integer.parseInt(node
                            .getChildText(ModelNodeConf.STRENGTH)));
            toughness = factory.getAttribute(ModelNodeConf.TOUGHNESS, Integer
                    .parseInt(node.getChildText(ModelNodeConf.TOUGHNESS)));
            tech = factory.getAttribute(ModelNodeConf.TECH,
                    Integer.parseInt(node.getChildText(ModelNodeConf.TECH)));

            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            store = new UnitValorationStore(getRulesetService());
            valoration = new DefaultDerivedValueHandler("valoration", store);

            unit = new DefaultUnit(name, actions, agility, combat, precision,
                    strength, tech, toughness, cost,
                    new LinkedList<SpecialRule>(), valoration);

            store.setUnit(unit);

            units.put(name, unit);
        }

        return units;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
