package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;

public final class FactionNameFormatter extends
        AbstractValueFormatter<String, Faction> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public FactionNameFormatter() {
        super();
    }

    @Override
    public final String format(final Faction value,
            final ReportParameters reportParameters) {
        return value.getNameToken();
    }

}
