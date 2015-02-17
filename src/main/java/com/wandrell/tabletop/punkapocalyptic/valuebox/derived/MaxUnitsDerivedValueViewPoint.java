package com.wandrell.tabletop.punkapocalyptic.valuebox.derived;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.valuebox.derived.AbstractDerivedValueViewPoint;
import com.wandrell.tabletop.valuebox.derived.DerivedValueViewPoint;

public final class MaxUnitsDerivedValueViewPoint extends
        AbstractDerivedValueViewPoint {

    private Gang                 gang;
    private final GangListener   listener;
    private final RulesetService serviceRuleset;

    {
        final DerivedValueViewPoint source = this;
        listener = new GangListenerAdapter() {

            @Override
            public final void valorationChanged(final EventObject evt) {
                fireValueChangedEvent(new ValueChangeEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public MaxUnitsDerivedValueViewPoint(final Gang gang,
            final RulesetService service) {
        this(service);

        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;

        gang.addGangListener(getListener());
    }

    public MaxUnitsDerivedValueViewPoint(
            final MaxUnitsDerivedValueViewPoint store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        gang = store.gang;
        serviceRuleset = store.serviceRuleset;
    }

    public MaxUnitsDerivedValueViewPoint(final RulesetService service) {
        super();

        checkNotNull(service, "Received a null pointer as ruleset service");

        serviceRuleset = service;
    }

    @Override
    public final Integer getValue() {
        return getRulesetService().getMaxAllowedUnits(getGang());
    }

    public final void setGang(final Gang gang) {
        if (this.gang != null) {
            this.gang.addGangListener(getListener());
        }

        this.gang = gang;

        this.gang.addGangListener(getListener());
    }

    private final Gang getGang() {
        return gang;
    }

    private final GangListener getListener() {
        return listener;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
