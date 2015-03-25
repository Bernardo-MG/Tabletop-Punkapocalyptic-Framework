package com.wandrell.tabletop.punkapocalyptic.model.unit;

import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultUnit.DerivedValuesBuilder;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitActionsValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitAgilityValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitCombatValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitPrecisionValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitStrengthValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitTechValueBox;
import com.wandrell.tabletop.punkapocalyptic.valuebox.UnitToughnessValueBox;
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
    public final ValueBox getActions(final Integer baseValue, final Unit unit) {
        return new UnitActionsValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox getAgility(final Integer baseValue, final Unit unit) {
        return new UnitAgilityValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox getCombat(final Integer baseValue, final Unit unit) {
        return new UnitCombatValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox
            getPrecision(final Integer baseValue, final Unit unit) {
        return new UnitPrecisionValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox getStrength(final Integer baseValue, final Unit unit) {
        return new UnitStrengthValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox getTech(final Integer baseValue, final Unit unit) {
        return new UnitTechValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox
            getToughness(final Integer baseValue, final Unit unit) {
        return new UnitToughnessValueBox(baseValue, unit);
    }

    @Override
    public final ValueBox getValoration(final Unit unit) {
        return new UnitValorationValueBox(unit, service);
    }

}
