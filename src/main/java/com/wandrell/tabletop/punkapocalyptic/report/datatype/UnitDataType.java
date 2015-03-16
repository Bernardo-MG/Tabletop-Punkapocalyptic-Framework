package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;

public final class UnitDataType extends AbstractDataType<Unit, Unit> {

    private static final long                     serialVersionUID = 1L;
    private final DRIValueFormatter<String, Unit> formatter;

    public UnitDataType(final DRIValueFormatter<String, Unit> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Unit> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Unit value, final Locale locale) {
        return value.getName();
    }

}
