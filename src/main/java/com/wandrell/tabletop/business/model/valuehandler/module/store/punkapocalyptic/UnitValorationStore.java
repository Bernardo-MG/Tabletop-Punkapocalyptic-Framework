package com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.business.model.punkapocalyptic.event.ValorationListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.module.store.AbstractStoreModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.StoreModule;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.UnitAware;

public final class UnitValorationStore extends AbstractStoreModule implements
        UnitAware {

    private final ValorationListener listener;
    private final RulesetService     serviceRuleset;
    private Unit                     unit;

    {
        final StoreModule source = this;
        listener = new ValorationListener() {

            @Override
            public final void valorationChanged(final EventObject event) {
                fireValueChangedEvent(new ValueHandlerEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public UnitValorationStore(final RulesetService service) {
        super();

        checkNotNull(service, "Received a null pointer as ruleset service");

        serviceRuleset = service;
    }

    public UnitValorationStore(final Unit unit, final RulesetService service) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(service, "Received a null pointer as ruleset service");

        this.unit = unit;
        serviceRuleset = service;

        unit.addValorationListener(getListener());
    }

    public UnitValorationStore(final UnitValorationStore store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        unit = store.unit;
        serviceRuleset = store.serviceRuleset;
    }

    @Override
    public final UnitValorationStore createNewInstance() {
        return new UnitValorationStore(this);
    }

    @Override
    public final Integer getValue() {
        return getRulesetService().getUnitValoration(getUnit());
    }

    @Override
    public final void setUnit(final Unit unit) {
        checkNotNull(unit, "Received a null pointer as unit");

        if (this.unit != null) {
            this.unit.removeValorationListener(getListener());
        }

        this.unit = unit;

        unit.addValorationListener(getListener());
    }

    private final ValorationListener getListener() {
        return listener;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Unit getUnit() {
        return unit;
    }

}
