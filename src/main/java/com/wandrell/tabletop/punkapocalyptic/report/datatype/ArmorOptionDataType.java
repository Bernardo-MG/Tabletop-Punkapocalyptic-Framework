package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;

public final class ArmorOptionDataType extends
        AbstractDataType<ArmorOption, ArmorOption> {

    private static final long                            serialVersionUID = 1L;
    private final DRIValueFormatter<String, ArmorOption> formatter;

    public ArmorOptionDataType(
            final DRIValueFormatter<String, ArmorOption> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, ArmorOption> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final ArmorOption value,
            final Locale locale) {
        return value.getArmor().getNameToken();
    }

}
