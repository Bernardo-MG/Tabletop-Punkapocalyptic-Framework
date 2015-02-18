package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset.constraint;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter.ConstraintParameterFactory;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint.AbstractTestConstraint;

public class TestDependantConstraint extends AbstractTestConstraint {

    @DataProvider(name = NOT_VALIDATES)
    public final static Iterator<Object[]> getNotValid() throws Exception {
        return ConstraintParameterFactory.getInstance()
                .getNotValidDependantConstraintParameters();
    }

    @DataProvider(name = VALIDATES)
    public final static Iterator<Object[]> getValid() throws Exception {
        return ConstraintParameterFactory.getInstance()
                .getValidDependantConstraintParameters();
    }

    public TestDependantConstraint() {
        super();
    }

}
