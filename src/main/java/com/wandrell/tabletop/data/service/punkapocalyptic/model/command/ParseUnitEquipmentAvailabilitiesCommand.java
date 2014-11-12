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

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.ReturnCommand;

public final class ParseUnitEquipmentAvailabilitiesCommand implements
        ReturnCommand<Map<String, Collection<Equipment>>> {

    private final Document               doc;
    private final Map<String, Equipment> equipment;
    private final Map<String, Unit>      units;

    public ParseUnitEquipmentAvailabilitiesCommand(final Document doc,
            final Map<String, Unit> units,
            final Map<String, Equipment> equipment) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(units, "Received a null pointer as units");
        checkNotNull(equipment, "Received a null pointer as equipment");

        this.doc = doc;
        this.units = units;
        this.equipment = equipment;
    }

    @Override
    public final Map<String, Collection<Equipment>> execute() throws Exception {
        final Map<String, Collection<Equipment>> availabilities;

        availabilities = new LinkedHashMap<>();

        for (final Unit unit : getUnits().values()) {
            availabilities.put(unit.getUnitName(),
                    getEquipment(unit.getUnitName()));
        }

        return availabilities;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, Equipment> getEquipment() {
        return equipment;
    }

    private final Collection<Equipment> getEquipment(final String unit) {
        final Collection<Equipment> equipment;
        final Collection<Element> nodes;
        final Collection<Element> nodesFactions;
        final String expression;
        final String expFaction;
        final String faction;

        expFaction = String.format(
                "//faction_unit//unit[name='%s']/../../faction", unit);

        nodesFactions = XPathFactory.instance()
                .compile(expFaction, Filters.element()).evaluate(getDocument());
        faction = nodesFactions.iterator().next().getText();

        expression = String.format(
                "//faction_equipment_piece[faction='%s']//equipment_piece",
                faction);
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        equipment = new LinkedList<>();
        for (final Element node : nodes) {
            equipment.add(getEquipment().get(node.getChild("name").getText()));
        }

        return equipment;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

}
