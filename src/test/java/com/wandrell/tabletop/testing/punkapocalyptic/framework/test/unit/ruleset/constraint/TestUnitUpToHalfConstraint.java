package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset.constraint;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.ConstraintParameterFactory;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint.AbstractTestConstraint;

public class TestUnitUpToHalfConstraint extends AbstractTestConstraint {

    @DataProvider(name = NOT_VALIDATES)
    public final static Iterator<Object[]> getNotValid() {
        return ConstraintParameterFactory.getInstance()
                .getNotValidUpToHalfConstraintParameters();
    }

    @DataProvider(name = VALIDATES)
    public final static Iterator<Object[]> getValid() {
        return ConstraintParameterFactory.getInstance()
                .getValidUpToHalfConstraintParameters();
    }

    public TestUnitUpToHalfConstraint() {
        super();
        // TODO: Test with an empty list
    }

}
