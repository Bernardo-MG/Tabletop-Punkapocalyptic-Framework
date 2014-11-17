package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.test.unit.ruleset.constraint;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.procedure.ProcedureConstraint;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.GangAware;

public abstract class AbstractTestGangConstraint {

    protected static final String NOT_VALIDATES = "not_validates";
    protected static final String VALIDATES     = "validates";

    public AbstractTestGangConstraint() {
        super();
        // TODO: Test with an empty list
    }

    @Test(dataProvider = NOT_VALIDATES)
    public final void testValidate_NotValidates(
            final ProcedureConstraint constraint, final Gang gang) {
        ((GangAware) constraint).setGang(gang);
        Assert.assertTrue(!constraint.isValid());
    }

    @Test(dataProvider = VALIDATES)
    public final void testValidate_Validates(
            final ProcedureConstraint constraint, final Gang gang) {
        ((GangAware) constraint).setGang(gang);
        Assert.assertTrue(constraint.isValid());
    }

}
