package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.DefaultUnitArmorAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.availability.UnitArmorAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitArmorAvailabilitiesCommand implements
        ReturnCommand<Map<String, UnitArmorAvailability>> {

    private final Map<String, Armor> armors;
    private final Document           doc;
    private final Map<String, Unit>  units;

    public ParseUnitArmorAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units, final Map<String, Armor> armors) {
        super();

        this.doc = doc;
        this.units = units;
        this.armors = armors;
    }

    @Override
    public final Map<String, UnitArmorAvailability> execute() throws Exception {
        final Map<String, UnitArmorAvailability> availabilities;
        UnitArmorAvailability availability;

        availabilities = new LinkedHashMap<>();

        for (final Unit unit : getUnits().values()) {
            availability = buildAvailability(unit);

            availabilities.put(unit.getUnitName(), availability);
        }

        return availabilities;
    }

    private final UnitArmorAvailability buildAvailability(final Unit unit) {
        final Collection<Armor> armors;
        final Armor armor;
        final UnitArmorAvailability availability;

        armors = getArmors(unit.getUnitName());
        armor = getInitialArmor(unit.getUnitName());

        availability = new DefaultUnitArmorAvailability(armors, armor);

        return availability;
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

    private final Collection<Armor> getArmors(final String unit) {
        final Collection<Armor> armors;
        final Collection<Element> nodes;
        final String expression;
        Armor armor;

        armors = new LinkedList<>();

        expression = String.format(
                "//unit_armors/unit_armor[unit='%s']/armors/armor", unit);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        for (final Element node : nodes) {
            armor = getArmors().get(node.getChildText("name"));
            armor.setCost(Integer.parseInt(node.getChildText("cost")));
            armors.add(armor);
        }

        return armors;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Armor getInitialArmor(final String unit) {
        final Collection<Element> nodes;
        final Armor armor;
        Element armorNode;

        nodes = XPathFactory.instance()
                .compile("//unit_armor", Filters.element())
                .evaluate(getDocument());

        if (nodes.isEmpty()) {
            armor = null;
        } else {
            armorNode = nodes.iterator().next().getChild(ModelNodeConf.ARMOR);

            if (armorNode == null) {
                armor = null;
            } else {
                armor = getArmors().get(armorNode.getText())
                        .createNewInstance();
            }
        }

        return armor;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

}
