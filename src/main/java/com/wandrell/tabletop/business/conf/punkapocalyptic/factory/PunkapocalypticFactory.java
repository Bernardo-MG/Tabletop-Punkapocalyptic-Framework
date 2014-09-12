package com.wandrell.tabletop.business.conf.punkapocalyptic.factory;

import com.wandrell.tabletop.business.model.valuehandler.DefaultValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.generator.DefaultGenerator;
import com.wandrell.tabletop.business.model.valuehandler.module.interval.DefaultIntervalModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.DefaultStore;
import com.wandrell.tabletop.business.model.valuehandler.module.validator.IntervalValidator;

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
        return new DefaultValueHandler(name, new DefaultGenerator(),
                new DefaultIntervalModule(0, Integer.MAX_VALUE),
                new DefaultStore(value), new IntervalValidator());
    }

}
