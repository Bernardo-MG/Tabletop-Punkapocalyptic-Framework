package com.wandrell.tabletop.business.conf.punkapocalyptic.factory;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.FirearmWeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.DefaultGang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.valuehandler.EditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularEditableValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.module.generator.DefaultGenerator;
import com.wandrell.tabletop.business.model.valuehandler.module.interval.DefaultIntervalModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.DefaultStore;
import com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic.GangValorationStore;
import com.wandrell.tabletop.business.model.valuehandler.module.validator.IntervalValidator;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

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
        checkNotNull(name, "Received a null pointer as name");
        checkNotNull(value, "Received a null pointer as value");

        return new ModularEditableValueHandler(name, new DefaultGenerator(),
                new DefaultIntervalModule(1, 10), new DefaultStore(value),
                new IntervalValidator());
    }

    public final Gang getGang(final Faction faction,
            final RulesetService ruleService) {
        final ValueHandler valoration;
        final Gang gang;
        final GangValorationStore store;

        store = new GangValorationStore(ruleService);

        valoration = new ModularDerivedValueHandler("valoration", store);
        gang = new DefaultGang(faction, valoration);

        store.setGang(gang);

        return gang;
    }

    public final WeaponEnhancement getWeaponEnhancement(final String name,
            final Integer cost) {
        final WeaponEnhancement enhancement;

        checkNotNull(name, "Received a null pointer as name");
        checkNotNull(cost, "Received a null pointer as cost");

        if ("bayonet".equals(name)) {
            enhancement = new FirearmWeaponEnhancement("bayonet", cost);
        } else {
            enhancement = null;
        }

        return enhancement;
    }

}
