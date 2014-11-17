package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;

public abstract class AbstractTestGangConstraint {

    protected static final String NOT_VALIDATES = "not_validates";
    protected static final String VALIDATES     = "validates";

    public AbstractTestGangConstraint() {
        super();
    }

    @Test(dataProvider = NOT_VALIDATES)
    public final void testValidate_NotValidates(
            final GangConstraint constraint, final Gang gang) {
        Assert.assertTrue(!constraint.isValid(gang));
    }

    @Test(dataProvider = VALIDATES)
    public final void testValidate_Validates(final GangConstraint constraint,
            final Gang gang) {
        Assert.assertTrue(constraint.isValid(gang));
    }

}
