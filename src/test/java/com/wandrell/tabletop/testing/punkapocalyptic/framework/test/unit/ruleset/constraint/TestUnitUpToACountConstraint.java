package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset.constraint;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.ConstraintParameterFactory;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint.AbstractTestConstraint;

public class TestUnitUpToACountConstraint extends AbstractTestConstraint {

    @DataProvider(name = NOT_VALIDATES)
    public final static Iterator<Object[]> getNotValid() throws Exception {
        return ConstraintParameterFactory.getInstance()
                .getNotValidUpToCountConstraintParameters();
    }

    @DataProvider(name = VALIDATES)
    public final static Iterator<Object[]> getValid() throws Exception {
        return ConstraintParameterFactory.getInstance()
                .getValidUpToCountConstraintParameters();
    }

    public TestUnitUpToACountConstraint() {
        super();
        // TODO: Test with an empty list
    }

}
