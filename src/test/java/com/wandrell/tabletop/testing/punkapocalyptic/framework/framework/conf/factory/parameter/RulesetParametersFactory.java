package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import org.springframework.context.ApplicationContext;

import com.wandrell.conf.TestingConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.RulesetParametersConf;
import com.wandrell.util.ContextUtils;
import com.wandrell.util.FileUtils;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.TestUtils;

public final class RulesetParametersFactory {

    private static final RulesetParametersFactory instance            = new RulesetParametersFactory();
    private static Object                         lockMaxAllowedUnits = new Object();
    private static Collection<Collection<Object>> valuesMaxUnits;

    public static final RulesetParametersFactory getInstance() {
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

    public final Iterator<Object[]> getMaxAllowedUnitsValues() {
        final ApplicationContext context;
        final Properties properties;

        if (valuesMaxUnits == null) {
            synchronized (lockMaxAllowedUnits) {
                if (valuesMaxUnits == null) {
                    properties = FileUtils
                            .getProperties(ResourceUtils
                                    .getClassPathInputStream(RulesetParametersConf.PROPERTIES_MAX_ALLOWED_UNITS));
                    context = ContextUtils.getClassPathContext(properties,
                            TestingConf.CONTEXT_DEFAULT);

                    valuesMaxUnits = TestUtils.getParameters(context);
                }
            }
        }

        return getParameters(valuesMaxUnits);
    }

}
