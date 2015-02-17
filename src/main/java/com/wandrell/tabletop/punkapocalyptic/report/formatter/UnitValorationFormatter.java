package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.conf.ReportBundleConf;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;

public final class UnitValorationFormatter extends
        AbstractValueFormatter<String, Unit> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public UnitValorationFormatter(final LocalizationService service) {
        super();

        localizationService = service;
    }

    @Override
    public final String format(final Unit value,
            final ReportParameters reportParameters) {
        final String valueLabel;

        valueLabel = getLocalizationService().getReportString(
                ReportBundleConf.VALORATION);

        return String.format("%s: %d", valueLabel, value.getValoration());
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
