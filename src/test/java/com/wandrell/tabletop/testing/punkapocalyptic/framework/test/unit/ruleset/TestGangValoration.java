package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.repository.WeaponRepository;
import com.wandrell.tabletop.punkapocalyptic.service.DefaultRulesetService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class TestGangValoration {

    private RulesetService service;

    public TestGangValoration() {
        super();
    }

    @BeforeClass
    public final void initializeWeapons() {
        final Map<Object, Object> config;
        final WeaponRepository repo;

        config = new LinkedHashMap<>();

        config.put("bullet_cost", "10");

        repo = Mockito.mock(WeaponRepository.class);

        service = new DefaultRulesetService(config, repo);
    }

    @Test
    public final void testGangValoration() {
        Assert.assertEquals(service.getGangValoration(getGang()), (Integer) 46);
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

}
