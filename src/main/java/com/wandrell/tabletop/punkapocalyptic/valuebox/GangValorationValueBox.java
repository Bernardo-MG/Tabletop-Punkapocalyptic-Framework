package com.wandrell.tabletop.punkapocalyptic.valuebox;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.EventObject;

import com.wandrell.tabletop.event.ValueChangeEvent;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitEvent;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.valuebox.AbstractValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class GangValorationValueBox extends AbstractValueBox {

    private final Gang           gang;
    private final GangListener   listener;
    private final RulesetService serviceRuleset;
    private Integer              valoration;

    {
        final ValueBox source = this;
        listener = new GangListenerAdapter() {

            @Override
            public void bulletsChanged(final EventObject event) {
                final Integer old;

                old = valoration;

                valoration = getRulesetService().getGangValoration(getGang());
                fireValueChangedEvent(new ValueChangeEvent(source, old,
                        valoration));
            }

            @Override
            public void unitAdded(final UnitEvent event) {
                final Integer old;

                old = valoration;

                valoration = getRulesetService().getGangValoration(getGang());
                fireValueChangedEvent(new ValueChangeEvent(source, old,
                        valoration));
            }

            @Override
            public void unitRemoved(final UnitEvent event) {
                final Integer old;

                old = valoration;

                valoration = getRulesetService().getGangValoration(getGang());
                fireValueChangedEvent(new ValueChangeEvent(source, old,
                        valoration));
            }

        };
    }

    public GangValorationValueBox(final Gang gang, final RulesetService service) {
        super();

        checkNotNull(gang, "Received a null pointer as gang");
        checkNotNull(service, "Received a null pointer as service");

        this.gang = gang;

        serviceRuleset = service;

        valoration = getRulesetService().getGangValoration(getGang());

        gang.addGangListener(getListener());
    }

    public GangValorationValueBox(final GangValorationValueBox store) {
        super();

        checkNotNull(store, "Received a null pointer as store");

        gang = store.gang;

        serviceRuleset = store.serviceRuleset;

        valoration = getRulesetService().getGangValoration(getGang());
    }

    @Override
    public final GangValorationValueBox createNewInstance() {
        return new GangValorationValueBox(this);
    }

    @Override
    public final Integer getValue() {
        return valoration;
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
