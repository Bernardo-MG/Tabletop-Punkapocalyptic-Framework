package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Collection;
import java.util.LinkedList;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.service.ruleset.command.GetGangValorationCommand;

public final class TestGangValorationCommand {

    public TestGangValorationCommand() {
        super();
    }

    @Test
    public final void testGangValoration() {
        final GetGangValorationCommand command;

        command = new GetGangValorationCommand(getGang());
        command.setRulesetService(getRulesetService());

        command.execute();
        Assert.assertEquals(command.getResult(), (Integer) 46);
    }

    private final Gang getGang() {
        final Gang gang;
        final Collection<Unit> units;
        Unit unit;

        units = new LinkedList<>();

        gang = Mockito.mock(Gang.class);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(1);

        units.add(unit);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(2);

        units.add(unit);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(3);

        units.add(unit);

        Mockito.when(gang.getBullets()).thenReturn(4);

        Mockito.when(gang.getUnits()).thenReturn(units);

        return gang;
    }

    private final RulesetService getRulesetService() {
        final RulesetService service;

        service = Mockito.mock(RulesetService.class);
        Mockito.when(service.getBulletCost()).thenReturn(10);

        return service;
    }

}
