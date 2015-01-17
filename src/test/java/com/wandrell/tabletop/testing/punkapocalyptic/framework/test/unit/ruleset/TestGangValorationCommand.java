package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Collection;
import java.util.LinkedList;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuebox.EditableValueBox;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command.GetGangValorationCommand;

public final class TestGangValorationCommand {

    public TestGangValorationCommand() {
        super();
    }

    @Test
    public final void testGangValoration() {
        final GetGangValorationCommand command;

        command = new GetGangValorationCommand(getGang());
        command.setRulesetService(getRulesetService());

        Assert.assertEquals(command.execute(), (Integer) 46);
    }

    private final Gang getGang() {
        final Gang gang;
        final Collection<Unit> units;
        final EditableValueBox bullets;
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

        bullets = Mockito.mock(EditableValueBox.class);
        Mockito.when(bullets.getValue()).thenReturn(4);
        Mockito.when(gang.getBullets()).thenReturn(bullets);

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
