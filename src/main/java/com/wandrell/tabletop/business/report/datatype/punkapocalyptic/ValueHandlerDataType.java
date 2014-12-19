package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.valuebox.ValueBox;

public final class ValueHandlerDataType extends
        AbstractDataType<ValueBox, ValueBox> {

    private static final long                         serialVersionUID = 1L;
    private final DRIValueFormatter<String, ValueBox> formatter        = new ValueHandlerFormatter();

    private class ValueHandlerFormatter extends
            AbstractValueFormatter<String, ValueBox> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(ValueBox value, ReportParameters reportParameters) {
            return String.valueOf(value.getValue());
        }
    }

    public ValueHandlerDataType() {
        super();
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
