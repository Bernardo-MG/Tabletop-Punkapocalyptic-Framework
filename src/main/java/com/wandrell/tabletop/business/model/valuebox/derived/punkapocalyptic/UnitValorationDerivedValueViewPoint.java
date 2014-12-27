package com.wandrell.tabletop.business.model.valuebox.derived.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitListener;
import com.wandrell.tabletop.business.model.valuebox.derived.AbstractDerivedValueViewPoint;
import com.wandrell.tabletop.business.model.valuebox.derived.DerivedValueViewPoint;
import com.wandrell.tabletop.business.model.valuebox.event.ValueBoxEvent;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

public final class UnitValorationDerivedValueViewPoint extends
        AbstractDerivedValueViewPoint {

    private final UnitListener   listener;
    private final RulesetService serviceRuleset;
    private final Unit           unit;

    {
        final DerivedValueViewPoint source = this;
        listener = new UnitListener() {

            @Override
            public final void statusChanged(final EventObject e) {}

            @Override
            public final void valorationChanged(final EventObject event) {
                fireValueChangedEvent(new ValueBoxEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public UnitValorationDerivedValueViewPoint(final Unit unit,
            final RulesetService service) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(service, "Received a null pointer as ruleset service");

        this.unit = unit;
        serviceRuleset = service;

        unit.addUnitListener(getListener());
    }

    public UnitValorationDerivedValueViewPoint(
            final UnitValorationDerivedValueViewPoint store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        unit = store.unit;
        serviceRuleset = store.serviceRuleset;
    }

    @Override
    public final Integer getValue() {
        return getRulesetService().getUnitValoration(getUnit());
    }

    private final UnitListener getListener() {
        return listener;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final Unit getUnit() {
        return unit;
    }

}
