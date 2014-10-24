package com.wandrell.tabletop.business.conf.punkapocalyptic.factory;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.FirearmWeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.valuehandler.EditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularEditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.generator.DefaultGenerator;
import com.wandrell.tabletop.business.model.valuehandler.module.interval.DefaultIntervalModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.DefaultStore;
import com.wandrell.tabletop.business.model.valuehandler.module.validator.IntervalValidator;

public final class PunkapocalypticFactory {

    private static final PunkapocalypticFactory instance = new PunkapocalypticFactory();

    public static final PunkapocalypticFactory getInstance() {
        return instance;
    }

    private PunkapocalypticFactory() {
        super();
    }

    public final EditableValueHandler getAttribute(final String name,
            final Integer value) {
        return new ModularEditableValueHandler(name, new DefaultGenerator(),
                new DefaultIntervalModule(1, 10), new DefaultStore(value),
                new IntervalValidator());
    }

    public final WeaponEnhancement getWeaponEnhancement(final String name,
            final Integer cost) {
        final WeaponEnhancement enhancement;

        if ("bayonet".equals(name)) {
            enhancement = new FirearmWeaponEnhancement("bayonet", cost);
        } else {
            enhancement = null;
        }

        return enhancement;
    }

}
