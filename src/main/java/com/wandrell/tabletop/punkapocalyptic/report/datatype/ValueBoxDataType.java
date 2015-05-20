package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.stats.valuebox.ValueBox;

public final class ValueBoxDataType extends
        AbstractDataType<ValueBox, ValueBox> {

    private static final long                         serialVersionUID = 1L;
    private final DRIValueFormatter<String, ValueBox> formatter;

    private class ValueBoxValueFormatter extends
            AbstractValueFormatter<String, ValueBox> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(ValueBox value, ReportParameters reportParameters) {
            return String.valueOf(value.getValue());
        }
    }

    public ValueBoxDataType() {
        super();

        formatter = new ValueBoxValueFormatter();
    }

    public ValueBoxDataType(final DRIValueFormatter<String, ValueBox> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, ValueBox> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String
            valueToString(final ValueBox value, final Locale locale) {
        return String.valueOf(value.getValue());
    }

}
