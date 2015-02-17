package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;

public final class FactionNameFormatter extends
        AbstractValueFormatter<String, Faction> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService service;

    public FactionNameFormatter(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String format(final Faction value,
            final ReportParameters reportParameters) {
        return getLocalizationService().getFactionNameString(value.getName());
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
