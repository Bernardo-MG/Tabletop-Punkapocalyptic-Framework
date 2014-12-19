package com.wandrell.tabletop.business.model.valuebox.derived.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.business.model.punkapocalyptic.event.ValorationListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.valuebox.derived.AbstractDerivedValueViewPoint;
import com.wandrell.tabletop.business.model.valuebox.derived.DerivedValueViewPoint;
import com.wandrell.tabletop.business.model.valuebox.event.ValueBoxEvent;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.GangAware;

public final class GangValorationDerivedValueViewPoint extends
        AbstractDerivedValueViewPoint implements GangAware {

    private Gang                     gang;
    private final ValorationListener listener;
    private final RulesetService     serviceRuleset;

    {
        final DerivedValueViewPoint source = this;
        listener = new ValorationListener() {

            @Override
            public final void valorationChanged(final EventObject event) {
                fireValueChangedEvent(new ValueBoxEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public GangValorationDerivedValueViewPoint(final Gang gang,
            final RulesetService service) {
        super();

        checkNotNull(gang, "Received a null pointer as gang");
        checkNotNull(service, "Received a null pointer as service");

        this.gang = gang;
        serviceRuleset = service;

        gang.addValorationListener(getListener());
    }

    public GangValorationDerivedValueViewPoint(
            final GangValorationDerivedValueViewPoint store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        gang = store.gang;
        serviceRuleset = store.serviceRuleset;
    }

    @Override
    public final Integer getValue() {
        return getRulesetService().getGangValoration(getGang());
    }

    @Override
    public final void setGang(final Gang gang) {
        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;

        gang.addValorationListener(getListener());
    }

    private final Gang getGang() {
        return gang;
    }

    private final ValorationListener getListener() {
        return listener;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
