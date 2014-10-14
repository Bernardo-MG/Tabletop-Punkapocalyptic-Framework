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

    public final EditableValueHandler getAttribute(final String name,
            final Integer value) {
        return new ModularEditableValueHandler(name, new DefaultGenerator(),
                new DefaultIntervalModule(1, 10), new DefaultStore(value),
                new IntervalValidator());
    }

    public final WeaponEnhancement getWeaponEnhancement(final String name,
            final Integer cost) {
        final WeaponEnhancement enhancement;

        if (name.equals("bayonet")) {
            enhancement = new FirearmWeaponEnhancement("bayonet", cost);
        } else {
            enhancement = null;
        }

        return enhancement;
    }

}
