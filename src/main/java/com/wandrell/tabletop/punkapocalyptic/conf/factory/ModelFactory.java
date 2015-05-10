package com.wandrell.tabletop.punkapocalyptic.conf.factory;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGang.ValorationBuilder;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.UnitTemplate;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.valuebox.GangValorationValueBox;
import com.wandrell.tabletop.valuebox.ValueBox;

public final class ModelFactory {

    private static final ModelFactory INSTANCE = new ModelFactory();

    public static final ModelFactory getInstance() {
        return INSTANCE;
    }

    private ModelFactory() {
        super();
        // TODO: Remove and do this on the commands or the service
    }

    public final Gang getGang(final Faction faction,
            final RulesetService service) {
        final ValorationBuilder valorationBuilder;
        final Gang gang;

        valorationBuilder = new DefaultGang.ValorationBuilder() {

            @Override
            public final ValueBox getValoration(final Gang gang) {
                return new GangValorationValueBox(gang, service);
            }

        };
        gang = new DefaultGang(faction, valorationBuilder);

        return gang;
    }

    public final Unit getUnit(final UnitTemplate template,
            final RulesetService service) {
        final Collection<SpecialRule> filtered;
        Unit unit;

        filtered = Collections2.filter(template.getSpecialRules(),
                new Predicate<SpecialRule>() {

                    @Override
                    public final boolean apply(final SpecialRule input) {
                        return input.getNameToken().equals("pack");
                    }

                });

        if (filtered.isEmpty()) {
            unit = new DefaultUnit(template);
        } else {
            unit = new DefaultGroupedUnit(template);
        }

        return unit;
    }

}
