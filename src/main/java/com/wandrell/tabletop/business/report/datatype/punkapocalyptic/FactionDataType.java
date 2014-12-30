package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;

public final class FactionDataType extends AbstractDataType<Faction, Faction> {

    private static final long                        serialVersionUID = 1L;
    private final DRIValueFormatter<String, Faction> formatter;

    public FactionDataType(final DRIValueFormatter<String, Faction> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Faction> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Faction value, final Locale locale) {
        return value.getName();
    }

}
