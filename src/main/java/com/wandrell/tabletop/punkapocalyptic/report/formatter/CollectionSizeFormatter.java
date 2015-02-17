package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import java.util.Collection;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

public final class CollectionSizeFormatter extends
        AbstractValueFormatter<String, Collection<?>> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public CollectionSizeFormatter() {
        super();
    }

    @Override
    public final String format(final Collection<?> value,
            final ReportParameters reportParameters) {
        return String.valueOf(value.size());
    }

}
