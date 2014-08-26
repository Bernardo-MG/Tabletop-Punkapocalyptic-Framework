package com.wandrell.tabletop.punkapocalyptic.framework.conf.factory;

import java.util.Properties;

import org.springframework.context.ApplicationContext;

import com.wandrell.tabletop.punkapocalyptic.framework.conf.PunkapocalypticFactoryConf;
import com.wandrell.tabletop.valuehandler.ValueHandler;
import com.wandrell.util.ContextUtils;
import com.wandrell.util.FileUtils;
import com.wandrell.util.PathUtils;

public final class PunkapocalypticFactory {

    private static PunkapocalypticFactory instance;

    public static final synchronized PunkapocalypticFactory getInstance() {
        if (instance == null) {
            instance = new PunkapocalypticFactory();
        }

        return instance;
    }

    private PunkapocalypticFactory() {
        super();
    }

    public final ValueHandler getAttribute(final String name,
            final Integer value) {
        final ApplicationContext context;
        final Properties properties;

        properties = FileUtils
                .getProperties(PathUtils
                        .getClassPathResource(PunkapocalypticFactoryConf.PROPERTIES_ATTRIBUTE));

        // TODO: This is hardcoded
        properties.setProperty("attribute.name", name);
        properties
                .setProperty("attribute.module.store.value", value.toString());

        // TODO: Try to reload changing only the values
        context = ContextUtils
                .getContext(
                        PathUtils
                                .getClassPathResource(PunkapocalypticFactoryConf.CONTEXT_ATTRIBUTE),
                        properties);

        // Spring framework builds the instance

        return (ValueHandler) context
                .getBean(PunkapocalypticFactoryConf.BEAN_ATTRIBUTE);
    }

}
