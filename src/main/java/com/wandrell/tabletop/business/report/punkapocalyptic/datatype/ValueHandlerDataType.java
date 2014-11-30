package com.wandrell.tabletop.business.report.punkapocalyptic.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;

public final class ValueHandlerDataType extends
        AbstractDataType<ValueHandler, ValueHandler> {

    private static final long                             serialVersionUID = 1L;
    private final DRIValueFormatter<String, ValueHandler> formatter        = new ValueHandlerFormatter();

    private class ValueHandlerFormatter extends
            AbstractValueFormatter<String, ValueHandler> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(ValueHandler value,
                ReportParameters reportParameters) {
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
    public final DRIValueFormatter<String, ValueHandler> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final ValueHandler value,
            final Locale locale) {
        return String.valueOf(value.getValue());
    }

}
