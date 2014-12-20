package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Collection;
import java.util.LinkedList;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.GroupedUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.MutantUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Mutation;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuebox.EditableValueBox;
import com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command.GetUnitValorationCommand;
import com.wandrell.util.command.ReturnCommand;

public final class TestUnitValorationCommand {

    public TestUnitValorationCommand() {
        super();
    }

    @Test
    public final void testValoration_BaseUnit() throws Exception {
        final ReturnCommand<Integer> command;

        command = new GetUnitValorationCommand(getUnit());

        Assert.assertEquals(command.execute(), (Integer) 15);
    }

    @Test
    public final void testValoration_GroupedUnit() throws Exception {
        final ReturnCommand<Integer> command;

        command = new GetUnitValorationCommand(getGroupedUnit());

        Assert.assertEquals(command.execute(), (Integer) 30);
    }

    @Test
    public final void testValoration_MutantUnit() throws Exception {
        final ReturnCommand<Integer> command;

        command = new GetUnitValorationCommand(getMutantUnit());

        Assert.assertEquals(command.execute(), (Integer) 28);
    }

    private final GroupedUnit getGroupedUnit() {
        final GroupedUnit unit;
        final Collection<Equipment> equipment;
        final Collection<Weapon> weapons;
        final EditableValueBox size;
        Equipment equip;
        Weapon weapon;

        equipment = new LinkedList<>();
        weapons = new LinkedList<>();

        unit = Mockito.mock(GroupedUnit.class);

        Mockito.when(unit.getBaseCost()).thenReturn(1);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(2);

        weapons.add(weapon);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(3);

        weapons.add(weapon);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(4);

        equipment.add(equip);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(5);

        equipment.add(equip);

        size = Mockito.mock(EditableValueBox.class);
        Mockito.when(size.getValue()).thenReturn(2);

        Mockito.when(unit.getEquipment()).thenReturn(equipment);
        Mockito.when(unit.getWeapons()).thenReturn(weapons);
        Mockito.when(unit.getGroupSize()).thenReturn(size);

        return unit;
    }

    private final MutantUnit getMutantUnit() {
        final MutantUnit unit;
        final Collection<Equipment> equipment;
        final Collection<Weapon> weapons;
        final Collection<Mutation> mutations;
        Mutation mutation;
        Equipment equip;
        Weapon weapon;

        equipment = new LinkedList<>();
        weapons = new LinkedList<>();
        mutations = new LinkedList<>();

        unit = Mockito.mock(MutantUnit.class);

        Mockito.when(unit.getBaseCost()).thenReturn(1);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(2);

        weapons.add(weapon);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(3);

        weapons.add(weapon);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(4);

        equipment.add(equip);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(5);

        equipment.add(equip);

        mutation = Mockito.mock(Mutation.class);
        Mockito.when(mutation.getCost()).thenReturn(6);

        mutations.add(mutation);

        mutation = Mockito.mock(Mutation.class);
        Mockito.when(mutation.getCost()).thenReturn(7);

        mutations.add(mutation);

        Mockito.when(unit.getEquipment()).thenReturn(equipment);
        Mockito.when(unit.getWeapons()).thenReturn(weapons);
        Mockito.when(unit.getMutations()).thenReturn(mutations);

        return unit;
    }

    private final Unit getUnit() {
        final Unit unit;
        final Collection<Equipment> equipment;
        final Collection<Weapon> weapons;
        Equipment equip;
        Weapon weapon;

        equipment = new LinkedList<>();
        weapons = new LinkedList<>();

        unit = Mockito.mock(Unit.class);

        Mockito.when(unit.getBaseCost()).thenReturn(1);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(2);

        weapons.add(weapon);

        weapon = Mockito.mock(Weapon.class);
        Mockito.when(weapon.getCost()).thenReturn(3);

        weapons.add(weapon);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(4);

        equipment.add(equip);

        equip = Mockito.mock(Equipment.class);
        Mockito.when(equip.getCost()).thenReturn(5);

        equipment.add(equip);

        Mockito.when(unit.getEquipment()).thenReturn(equipment);
        Mockito.when(unit.getWeapons()).thenReturn(weapons);

        return unit;
    }

}
