package com.wandrell.tabletop.punkapocalyptic.data.service.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseUnitEquipmentAvailabilitiesCommand implements
        ReturnCommand<Collection<UnitEquipmentAvailability>>, ModelServiceAware {

    private final Document              doc;
    private final Repository<Equipment> equipmentRepo;
    private ModelService                modelService;
    private final Repository<Unit>      unitRepo;

    public ParseUnitEquipmentAvailabilitiesCommand(final Document doc,
            final Repository<Unit> unitRepo,
            final Repository<Equipment> equipmentRepo) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(unitRepo, "Received a null pointer as units repository");
        checkNotNull(equipmentRepo,
                "Received a null pointer as equipment repository");

        this.doc = doc;
        this.unitRepo = unitRepo;
        this.equipmentRepo = equipmentRepo;
    }

    @Override
    public final Collection<UnitEquipmentAvailability> execute()
            throws Exception {
        final Collection<UnitEquipmentAvailability> availabilities;

        availabilities = new LinkedList<>();

        for (final Unit unit : getUnitsRepository().getCollection(u -> true)) {
            availabilities.add(getModelService().getUnitEquipmentAvailability(
                    unit, getEquipment(unit.getUnitName())));
        }

        return availabilities;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return doc;
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
            equipment.add(getEquipmentRepository()
                    .getCollection(
                            e -> e.getName().equals(node.getChildText("name")))
                    .iterator().next());
        }

        return equipment;
    }

    private final Repository<Equipment> getEquipmentRepository() {
        return equipmentRepo;
    }

    private final ModelService getModelService() {
        return modelService;
    }

    private final Repository<Unit> getUnitsRepository() {
        return unitRepo;
    }

}
