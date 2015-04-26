package com.wandrell.tabletop.punkapocalyptic.model.unit;

import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitValorationValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class RulesetServiceDerivedValuesBuilder implements
        DerivedValuesBuilder {

    private final RulesetService service;

    public RulesetServiceDerivedValuesBuilder(final RulesetService service) {
        super();

        this.service = service;
    }

    @Override
    public final ValueBox getValoration(final Unit unit) {
        return new UnitValorationValueBox(unit, service);
    }

}
