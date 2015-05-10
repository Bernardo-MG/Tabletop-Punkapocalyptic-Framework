package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;

public final class ArmorOptionNameFormatter extends
        AbstractValueFormatter<String, ArmorOption> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public ArmorOptionNameFormatter() {
        super();
    }

    @Override
    public final String format(final ArmorOption value,
            final ReportParameters reportParameters) {
        return value.getArmor().getNameToken();
    }

}
