package com.wandrell.tabletop.punkapocalyptic.valuebox;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.valuebox.AbstractValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class UnitValorationValueBox extends AbstractValueBox {

    private final UnitListener   listener;
    private final RulesetService serviceRuleset;
    private final Unit           unit;

    {
        final ValueBox source = this;
        listener = new UnitListenerAdapter() {

            @Override
            public final void valorationChanged(final EventObject event) {
                fireValueChangedEvent(new ValueChangeEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public UnitValorationValueBox(final Unit unit, final RulesetService service) {
        super();

        checkNotNull(unit, "Received a null pointer as unit");
        checkNotNull(service, "Received a null pointer as ruleset service");

        this.unit = unit;
        serviceRuleset = service;

        unit.addUnitListener(getListener());
    }

    public UnitValorationValueBox(final UnitValorationValueBox store) {
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
