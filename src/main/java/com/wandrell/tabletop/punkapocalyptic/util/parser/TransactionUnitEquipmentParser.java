package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.punkapocalyptic.model.availability.DefaultUnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitEquipmentAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;

public final class TransactionUnitEquipmentParser implements
        Parser<Map<String, Object>, UnitEquipmentAvailability> {

    private final Repository<Equipment> equipmentRepo;
    private final Repository<Unit>      unitsRepo;

    public TransactionUnitEquipmentParser(final Repository<Unit> unitsRepo,
            final Repository<Equipment> equipmentRepo) {
        super();

        this.unitsRepo = unitsRepo;
        this.equipmentRepo = equipmentRepo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final UnitEquipmentAvailability
            parse(final Map<String, Object> input) {
        final Unit unit;
        final Collection<String> equipmentNames;
        final Collection<Equipment> equipment;

        unit = unitsRepo.getCollection(new Predicate<Unit>() {

            @Override
            public final boolean apply(final Unit unit) {
                return unit.getName().equals(input.get("unit"));
            }

        }).iterator().next();

        equipmentNames = (Collection<String>) input.get("equipment");
        equipment = equipmentRepo.getCollection(new Predicate<Equipment>() {

            @Override
            public final boolean apply(final Equipment piece) {
                return equipmentNames.contains(piece.getName());
            }

        });

        // TODO: Use a service
        return new DefaultUnitEquipmentAvailability(unit, equipment);
    }

}
