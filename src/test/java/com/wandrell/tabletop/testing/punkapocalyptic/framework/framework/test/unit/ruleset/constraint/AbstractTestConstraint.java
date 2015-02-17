package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.procedure.Constraint;

public abstract class AbstractTestConstraint {

    protected static final String NOT_VALIDATES = "not_validates";
    protected static final String VALIDATES     = "validates";

    public AbstractTestConstraint() {
        super();
    }

    @Test(dataProvider = NOT_VALIDATES)
    public final void testValidate_NotValidates(final Constraint constraint) {
        Assert.assertTrue(!constraint.isValid());
    }

    @Test(dataProvider = VALIDATES)
    public final void testValidate_Validates(final Constraint constraint) {
        Assert.assertTrue(constraint.isValid());
    }

}
