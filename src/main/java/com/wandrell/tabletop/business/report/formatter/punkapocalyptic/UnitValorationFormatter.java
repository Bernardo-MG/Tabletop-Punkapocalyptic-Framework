package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

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
