package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import com.wandrell.conf.TestingConf;
import com.wandrell.tabletop.business.model.procedure.constraint.punkapocalyptic.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.GangUnitsUpToLimitConstraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.UnitUpToACountConstraint;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.ConstraintParametersConf;
import com.wandrell.util.ContextUtils;
import com.wandrell.util.FileUtils;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.TestUtils;

public final class ConstraintParameterFactory {

    private static final ConstraintParameterFactory instance = new ConstraintParameterFactory();

    public static final ConstraintParameterFactory getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    private static final Iterator<Object[]> getUnitLimitConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        GangConstraint constraint;
        ValueHandler limit;
        Integer valueLimit;
        Integer unitsCount;
        Collection<Unit> units;
        Gang gang;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            valueLimit = (Integer) itrValues.next();
            limit = Mockito.mock(ValueHandler.class);
            Mockito.when(limit.getValue()).thenReturn(valueLimit);
            Mockito.when(limit.toString()).thenReturn(valueLimit.toString());

            constraint = new GangUnitsUpToLimitConstraint(limit, "message");

            gang = Mockito.mock(Gang.class);

            unitsCount = (Integer) itrValues.next();
            units = Mockito.mock(Collection.class);
            Mockito.when(units.size()).thenReturn(unitsCount);
            Mockito.when(gang.getUnits()).thenReturn(units);
            Mockito.when(gang.toString()).thenReturn(
                    String.format("Gang with %d units", unitsCount));

            result.add(new Object[] { constraint, gang });
        }

        return result.iterator();
    }

    @SuppressWarnings("unchecked")
    private static final Iterator<Object[]> getUpToCountConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        Collection<String> unitNames;
        Collection<Unit> units;
        GangConstraint constraint;
        Gang gang;
        Unit unit;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            constraint = new UnitUpToACountConstraint(
                    (String) itrValues.next(), (Integer) itrValues.next(),
                    "message");

            unitNames = (Collection<String>) itrValues.next();
            units = new LinkedList<>();
            for (final String u : unitNames) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(u);

                units.add(unit);
            }

            gang = Mockito.mock(Gang.class);

            Mockito.when(gang.getUnits()).thenReturn(units);

            result.add(new Object[] { constraint, gang });
        }

        return result.iterator();
    }

    private ConstraintParameterFactory() {
        super();
    }

    public final Iterator<Object[]> getNotValidUnitLimitConstraintParameters() {
        return getUnitLimitConstraintParameters(getUnitLimitValues(false));
    }

    public final Iterator<Object[]> getNotValidUpToCountConstraintParameters() {
        return getUpToCountConstraintParameters(getUpToCountValues(false));
    }

    public final Iterator<Object[]> getValidUnitLimitConstraintParameters() {
        return getUnitLimitConstraintParameters(getUnitLimitValues(true));
    }

    public final Iterator<Object[]> getValidUpToCountConstraintParameters() {
        return getUpToCountConstraintParameters(getUpToCountValues(true));
    }

    private final Collection<Collection<Object>> getUnitLimitValues(
            final Boolean valid) {
        final ApplicationContext context;
        final Properties properties;
        final Collection<String> required;
        final Collection<String> rejected;

        properties = FileUtils
                .getProperties(ResourceUtils
                        .getClassPathInputStream(ConstraintParametersConf.UNIT_LIMIT_PROPERTIES));
        context = ContextUtils.getClassPathContext(properties,
                TestingConf.CONTEXT_DEFAULT);

        required = new LinkedList<>();
        rejected = new LinkedList<>();

        if (valid) {
            required.add("valid");
        } else {
            rejected.add("valid");
        }

        return TestUtils.getParameters(context, required, rejected);
    }

    private final Collection<Collection<Object>> getUpToCountValues(
            final Boolean valid) {
        final ApplicationContext context;
        final Properties properties;
        final Collection<String> required;
        final Collection<String> rejected;

        properties = FileUtils
                .getProperties(ResourceUtils
                        .getClassPathInputStream(ConstraintParametersConf.UP_TO_A_COUNT_PROPERTIES));
        context = ContextUtils.getClassPathContext(properties,
                TestingConf.CONTEXT_DEFAULT);

        required = new LinkedList<>();
        rejected = new LinkedList<>();

        if (valid) {
            required.add("valid");
        } else {
            rejected.add("valid");
        }

        return TestUtils.getParameters(context, required, rejected);
    }

}
