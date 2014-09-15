package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.DefaultDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.EditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic.UnitValorationStore;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public class UnitsXMLDocumentReader implements
        XMLDocumentReader<Collection<Unit>> {

    private final RulesetDAO     dao;
    private final RulesetService serviceRuleset;

    public UnitsXMLDocumentReader(final RulesetDAO dao,
            final RulesetService service) {
        super();

        this.dao = dao;
        serviceRuleset = service;
    }

    @Override
    public final Collection<Unit> getValue(final Document doc) {
        final Collection<Unit> units;
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

        units = new LinkedList<>();
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

            units.add(unit);
        }

        return units;
    }

    protected final RulesetDAO getRulesetDAO() {
        return dao;
    }

    protected final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
