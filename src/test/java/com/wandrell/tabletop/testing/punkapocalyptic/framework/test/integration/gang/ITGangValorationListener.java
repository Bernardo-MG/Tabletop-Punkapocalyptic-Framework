package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.integration.gang;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGang.ValorationBuilder;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.service.DefaultRulesetService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.GangValorationValueBox;
import com.wandrell.tabletop.stats.event.ValueChangeEvent;
import com.wandrell.tabletop.stats.valuebox.ValueBox;

public final class ITGangValorationListener {

    private final class TestGangListener extends GangListenerAdapter {
        public Integer valoration;

        @Override
        public final void valorationChanged(final ValueChangeEvent event) {
            valoration = ((DefaultGang) event.getSource()).getValoration();
        }

    }

    public ITGangValorationListener() {
        super();
    }

    @Test
    public final void testBulletsChange() {
        final Gang gang;
        final TestGangListener listener;

        gang = getGang();

        listener = new TestGangListener();

        gang.addGangListener(listener);

        gang.setBullets(0);

        Assert.assertEquals(listener.valoration, (Integer) 6);

        gang.setBullets(1);

        Assert.assertEquals(listener.valoration, (Integer) 16);
    }

    private final Gang getGang() {
        final Gang gang;
        final Faction faction;
        final ValorationBuilder valorationBuilder;
        final RulesetService service;
        final Map<Object, Object> config;
        Unit unit;

        config = new LinkedHashMap<>();

        config.put("bullet_cost", "10");

        service = new DefaultRulesetService(config);

        faction = Mockito.mock(Faction.class);

        valorationBuilder = new DefaultGang.ValorationBuilder() {

            @Override
            public final ValueBox getValoration(final Gang gang) {
                return new GangValorationValueBox(gang, service);
            }

        };

        gang = new DefaultGang(faction, valorationBuilder);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(1);

        gang.addUnit(unit);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(2);

        gang.addUnit(unit);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getValoration()).thenReturn(3);

        gang.addUnit(unit);

        gang.setBullets(4);

        return gang;
    }

}
