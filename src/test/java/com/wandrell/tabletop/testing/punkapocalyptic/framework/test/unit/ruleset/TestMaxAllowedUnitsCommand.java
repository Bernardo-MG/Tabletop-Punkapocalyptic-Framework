package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Iterator;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command.GetMaxAllowedUnitsCommand;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.DataProviderConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.RulesetParametersFactory;
import com.wandrell.util.command.ReturnCommand;

public final class TestMaxAllowedUnitsCommand {

    @DataProvider(name = DataProviderConf.GENERIC_PROVIDER)
    public final static Iterator<Object[]> getData() {
        return RulesetParametersFactory.getInstance()
                .getMaxAllowedUnitsValues();
    }

    public TestMaxAllowedUnitsCommand() {
        super();
    }

    @Test(dataProvider = DataProviderConf.GENERIC_PROVIDER)
    public final void testCommand(final Integer points, final Integer units)
            throws Exception {
        final ReturnCommand<Integer> command;
        final Gang gang;

        gang = Mockito.mock(Gang.class);
        Mockito.when(gang.getValoration()).thenReturn(points);

        command = new GetMaxAllowedUnitsCommand(gang);

        Assert.assertEquals(command.execute(), units);
    }

}
