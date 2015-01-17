package com.wandrell.tabletop.business.model.valuebox.derived.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.valuebox.derived.AbstractDerivedValueViewPoint;
import com.wandrell.tabletop.business.model.valuebox.derived.DerivedValueViewPoint;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.event.ValueChangeEvent;
import com.wandrell.tabletop.business.util.event.ValueChangeListener;

public final class MaxUnitsDerivedValueViewPoint extends
        AbstractDerivedValueViewPoint {

    private Gang                      gang;
    private final RulesetService      serviceRuleset;
    private final ValueChangeListener valorationListener;

    {
        final DerivedValueViewPoint source = this;
        valorationListener = new ValueChangeListener() {

            @Override
            public final void valueChanged(final ValueChangeEvent evt) {
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

        gang.getValoration().addValueChangeListener(getValorationListener());
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
            this.gang.getValoration().removeValueChangeListener(
                    getValorationListener());
        }

        this.gang = gang;

        this.gang.getValoration().addValueChangeListener(
                getValorationListener());
    }

    private final Gang getGang() {
        return gang;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final ValueChangeListener getValorationListener() {
        return valorationListener;
    }

}
