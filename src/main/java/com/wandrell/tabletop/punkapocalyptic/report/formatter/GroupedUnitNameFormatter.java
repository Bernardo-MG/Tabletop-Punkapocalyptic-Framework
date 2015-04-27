package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.unit.DefaultGroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;

public final class GroupedUnitNameFormatter extends
        AbstractValueFormatter<String, Unit> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public GroupedUnitNameFormatter() {
        super();
    }

    @Override
    public final String format(final Unit value,
            final ReportParameters reportParameters) {
        final String name;
        final String result;

        name = value.getName();
        if (value instanceof DefaultGroupedUnit) {
            result = String.format("%dx %s", ((DefaultGroupedUnit) value)
                    .getGroupSize().getValue(), name);
        } else {
            result = name;
        }

        return result;
    }

}
