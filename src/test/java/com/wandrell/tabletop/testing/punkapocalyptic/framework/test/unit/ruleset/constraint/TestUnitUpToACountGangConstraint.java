package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset.constraint;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.ConstraintParameterFactory;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint.AbstractTestGangConstraint;

public class TestUnitUpToACountGangConstraint extends
        AbstractTestGangConstraint {

    @DataProvider(name = NOT_VALIDATES)
    public final static Iterator<Object[]> getNotValid() {
        return ConstraintParameterFactory.getInstance()
                .getNotValidUpToCountConstraintParameters();
    }

    @DataProvider(name = VALIDATES)
    public final static Iterator<Object[]> getValid() {
        return ConstraintParameterFactory.getInstance()
                .getValidUpToCountConstraintParameters();
    }

    public TestUnitUpToACountGangConstraint() {
        super();
    }

}
