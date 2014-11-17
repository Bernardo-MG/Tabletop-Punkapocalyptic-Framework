package com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;
import com.wandrell.tabletop.business.model.valuehandler.module.store.AbstractStoreModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.StoreModule;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

public final class MaxUnitsStore extends AbstractStoreModule {

    private Gang                       gang;
    private final RulesetService       serviceRuleset;
    private final ValueHandlerListener valorationListener;

    {
        final StoreModule source = this;
        valorationListener = new ValueHandlerListener() {

            @Override
            public final void valueChanged(final ValueHandlerEvent evt) {
                fireValueChangedEvent(new ValueHandlerEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public MaxUnitsStore(final Gang gang, final RulesetService service) {
        this(service);

        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;

        gang.getValoration().addValueEventListener(getValorationListener());
    }

    public MaxUnitsStore(final MaxUnitsStore store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        gang = store.gang;
        serviceRuleset = store.serviceRuleset;
    }

    public MaxUnitsStore(final RulesetService service) {
        super();

        checkNotNull(service, "Received a null pointer as ruleset service");

        serviceRuleset = service;
    }

    @Override
    public MaxUnitsStore createNewInstance() {
        return new MaxUnitsStore(this);
    }

    @Override
    public final Integer getValue() {
        return getRulesetService().getMaxAllowedUnits(getGang());
    }

    public final void setGang(final Gang gang) {
        if (this.gang != null) {
            this.gang.getValoration().removeValueEventListener(
                    getValorationListener());
        }

        this.gang = gang;

        this.gang.getValoration()
                .addValueEventListener(getValorationListener());
    }

    private final Gang getGang() {
        return gang;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final ValueHandlerListener getValorationListener() {
        return valorationListener;
    }

}
