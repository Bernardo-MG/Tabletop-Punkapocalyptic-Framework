package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;

public final class GangDataType extends AbstractDataType<Gang, Gang> {

    private static final long                     serialVersionUID = 1L;
    private final DRIValueFormatter<String, Gang> formatter;

    public GangDataType(final DRIValueFormatter<String, Gang> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Gang> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Gang value, final Locale locale) {
        return value.toString();
    }

}
