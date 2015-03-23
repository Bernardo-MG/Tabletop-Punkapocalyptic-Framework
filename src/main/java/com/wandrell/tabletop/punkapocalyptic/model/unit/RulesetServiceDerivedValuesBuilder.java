package com.wandrell.tabletop.punkapocalyptic.model.unit;

import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultUnit.DerivedValuesBuilder;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitActionsDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitAgilityDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitCombatDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitPrecisionDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitStrengthDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitTechDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitToughnessDerivedValueViewPoint;
import com.wandrell.tabletop.punkapocalyptic.valuebox.derived.UnitValorationDerivedValueViewPoint;
import com.wandrell.tabletop.valuebox.ValueBox;
import com.wandrell.tabletop.valuebox.derived.DerivedValueBox;
import com.wandrell.tabletop.valuebox.derived.DerivedValueViewPoint;

public final class RulesetServiceDerivedValuesBuilder implements
        DerivedValuesBuilder {

    private final RulesetService service;

    public RulesetServiceDerivedValuesBuilder(final RulesetService service) {
        super();

        this.service = service;
    }

    @Override
    public final ValueBox getActions(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitActionsDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox getAgility(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitAgilityDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox getCombat(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitCombatDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox
            getPrecision(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitPrecisionDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox getStrength(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitStrengthDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox getTech(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitTechDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox
            getToughness(final Integer baseValue, final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitToughnessDerivedValueViewPoint(baseValue, unit);

        return new DerivedValueBox(view);
    }

    @Override
    public final ValueBox getValoration(final Unit unit) {
        final DerivedValueViewPoint view;

        view = new UnitValorationDerivedValueViewPoint(unit, service);

        return new DerivedValueBox(view);
    }

}
