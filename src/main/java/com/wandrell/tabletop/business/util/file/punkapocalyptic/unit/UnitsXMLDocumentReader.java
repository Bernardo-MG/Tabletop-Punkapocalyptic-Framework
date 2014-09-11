package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public class UnitsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Unit>> {

    private final Map<String, Collection<SpecialRule>> rules;

    public UnitsXMLDocumentReader(
            final Map<String, Collection<SpecialRule>> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Unit> getValue(final Document doc) {
        final Map<String, Unit> units;
        final Element root;
        final PunkapocalypticFactory factory;
        String name;
        ValueHandler actions;
        ValueHandler combat;
        ValueHandler precision;
        ValueHandler agility;
        ValueHandler strength;
        ValueHandler toughness;
        ValueHandler tech;
        ValueHandler slots;
        Integer cost;

        root = doc.getRootElement();

        factory = PunkapocalypticFactory.getInstance();

        units = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText("name");

            actions = factory.getAttribute("actions",
                    Integer.parseInt(node.getChildText("actions")));
            combat = factory.getAttribute("combat",
                    Integer.parseInt(node.getChildText("combat")));
            precision = factory.getAttribute("precision",
                    Integer.parseInt(node.getChildText("precision")));
            agility = factory.getAttribute("agility",
                    Integer.parseInt(node.getChildText("agility")));
            strength = factory.getAttribute("strength",
                    Integer.parseInt(node.getChildText("strength")));
            toughness = factory.getAttribute("toughness",
                    Integer.parseInt(node.getChildText("toughness")));
            tech = factory.getAttribute("tech",
                    Integer.parseInt(node.getChildText("tech")));

            cost = Integer.parseInt(node.getChildText("cost"));

            slots = factory.getAttribute("weapon_slots", 2);

            units.put(name, new DefaultUnit(name, actions, agility, combat,
                    precision, strength, tech, toughness, slots, cost,
                    getRules().get(name)));
        }

        return units;
    }

    protected final Map<String, Collection<SpecialRule>> getRules() {
        return rules;
    }

}
