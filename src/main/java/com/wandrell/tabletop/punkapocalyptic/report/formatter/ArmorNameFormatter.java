package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;

public final class ArmorNameFormatter extends
        AbstractValueFormatter<String, Armor> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public ArmorNameFormatter() {
        super();
    }

    @Override
    public final String format(final Armor value,
            final ReportParameters reportParameters) {
        return value.getName();
    }

}
