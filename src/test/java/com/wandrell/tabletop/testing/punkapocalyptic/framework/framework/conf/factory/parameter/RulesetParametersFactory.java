package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.springframework.context.ApplicationContext;

import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.ParameterContext;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.RulesetParametersConf;
import com.wandrell.util.ContextUtils;
import com.wandrell.util.FileUtils;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.TestUtils;

public final class RulesetParametersFactory {

    private static RulesetParametersFactory       instance;
    private static Collection<Collection<Object>> valuesMaxUnits;

    public static final synchronized RulesetParametersFactory getInstance() {
        if (instance == null) {
            instance = new RulesetParametersFactory();
        }

        return instance;
    }

    private static final Iterator<Object[]> getParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            result.add(values.toArray());
        }

        return result.iterator();
    }

    private RulesetParametersFactory() {
        super();
    }

    public final synchronized Iterator<Object[]> getMaxAllowedUnitsValues() {
        final ApplicationContext context;
        final Properties properties;

        if (valuesMaxUnits == null) {
            properties = FileUtils
                    .getProperties(ResourceUtils
                            .getClassPathInputStream(RulesetParametersConf.PROPERTIES_MAX_ALLOWED_UNITS));
            context = ContextUtils.getClassPathContext(
                    ParameterContext.DEFAULT, properties);

            valuesMaxUnits = TestUtils.getParameters(context);
        }

        return getParameters(valuesMaxUnits);
    }

}
