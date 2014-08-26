package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.punkapocalyptic.framework.conf.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.punkapocalyptic.unit.DefaultUnit;
import com.wandrell.tabletop.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.valuehandler.ValueHandler;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public class UnitsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Unit>> {

    public UnitsXMLDocumentReader() {
        super();
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

        root = doc.getRootElement();

        factory = PunkapocalypticFactory.getInstance();

        units = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getAttributeValue("name");

            actions = factory.getAttribute("actions",
                    Integer.parseInt(node.getAttributeValue("actions")));
            combat = factory.getAttribute("combat",
                    Integer.parseInt(node.getAttributeValue("combat")));
            precision = factory.getAttribute("precision",
                    Integer.parseInt(node.getAttributeValue("precision")));
            agility = factory.getAttribute("agility",
                    Integer.parseInt(node.getAttributeValue("agility")));
            strength = factory.getAttribute("strength",
                    Integer.parseInt(node.getAttributeValue("strength")));
            toughness = factory.getAttribute("toughness",
                    Integer.parseInt(node.getAttributeValue("toughness")));
            tech = factory.getAttribute("tech",
                    Integer.parseInt(node.getAttributeValue("tech")));

            slots = factory.getAttribute("weapon_slots", 2);

            units.put(name, new DefaultUnit(name, actions, agility, combat,
                    precision, strength, tech, toughness, slots));
        }

        return units;
    }

}
