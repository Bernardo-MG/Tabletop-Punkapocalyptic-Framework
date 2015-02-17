package com.wandrell.tabletop.punkapocalyptic.valuebox.derived;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.valuebox.derived.AbstractDerivedValueViewPoint;
import com.wandrell.tabletop.valuebox.derived.DerivedValueViewPoint;

public final class UnitValorationDerivedValueViewPoint extends
        AbstractDerivedValueViewPoint {

    private final UnitListener   listener;
    private final RulesetService serviceRuleset;
    private final Unit           unit;

    {
        final DerivedValueViewPoint source = this;
        listener = new UnitListenerAdapter() {

            @Override
            public final void valorationChanged(final EventObject event) {
                fireValueChangedEvent(new ValueChangeEvent(source,
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
