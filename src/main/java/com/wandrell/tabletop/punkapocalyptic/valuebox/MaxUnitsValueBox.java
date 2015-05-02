package com.wandrell.tabletop.punkapocalyptic.valuebox;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.valuebox.AbstractValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class MaxUnitsValueBox extends AbstractValueBox {

    private final Gang           gang;
    private final GangListener   listener;
    private Integer              max;
    private final RulesetService serviceRuleset;

    {
        final ValueBox source = this;
        listener = new GangListenerAdapter() {

            @Override
            public final void valorationChanged(final EventObject evt) {
                max = getRulesetService().getMaxAllowedUnits(
                        getGang().getValoration());
                fireValueChangedEvent(new ValueChangeEvent(source,
                        source.getValue(), source.getValue()));
            }

        };
    }

    public MaxUnitsValueBox(final Gang gang, final RulesetService service) {
        super();

        checkNotNull(service, "Received a null pointer as ruleset service");
        checkNotNull(gang, "Received a null pointer as gang");

        serviceRuleset = service;

        this.gang = gang;

        max = getRulesetService().getMaxAllowedUnits(getGang().getValoration());

        gang.addGangListener(getListener());
    }

    public MaxUnitsValueBox(final MaxUnitsValueBox value) {
        super();

        checkNotNull(value, "Received a null pointer as value box");

        gang = value.gang;

        serviceRuleset = value.serviceRuleset;

        max = getRulesetService().getMaxAllowedUnits(getGang().getValoration());
    }

    @Override
    public final MaxUnitsValueBox createNewInstance() {
        return new MaxUnitsValueBox(this);
    }

    @Override
    public final Integer getValue() {
        return max;
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
