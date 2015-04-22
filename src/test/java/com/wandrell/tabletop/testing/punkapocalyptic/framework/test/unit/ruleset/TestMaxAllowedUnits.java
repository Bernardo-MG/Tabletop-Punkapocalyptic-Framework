package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Iterator;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.repository.WeaponRepository;
import com.wandrell.tabletop.punkapocalyptic.service.DefaultRulesetService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.DataProviderConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.RulesetParametersFactory;

public final class TestMaxAllowedUnits {

    private RulesetService service;

    @DataProvider(name = DataProviderConf.GENERIC_PROVIDER)
    public final static Iterator<Object[]> getData() throws Exception {
        return RulesetParametersFactory.getInstance()
                .getMaxAllowedUnitsValues();
    }

    public TestMaxAllowedUnits() {
        super();
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public final void initializeWeapons() {
        final Map<Object, Object> config;
        final WeaponRepository repo;

        config = Mockito.mock(Map.class);
        repo = Mockito.mock(WeaponRepository.class);

        service = new DefaultRulesetService(config, repo);
    }

    @Test(dataProvider = DataProviderConf.GENERIC_PROVIDER)
    public final void testMaxAllowedUnits(final Integer points,
            final Integer units) throws Exception {
        final Gang gang;

        gang = Mockito.mock(Gang.class);
        Mockito.when(gang.getValoration()).thenReturn(points);

        Assert.assertEquals(service.getMaxAllowedUnits(gang), units);
    }

}
