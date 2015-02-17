package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.conf.ReportBundleConf;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;

public final class GangBulletsFormatter extends
        AbstractValueFormatter<String, Gang> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public GangBulletsFormatter(final LocalizationService localizationService) {
        super();

        this.localizationService = localizationService;
    }

    @Override
    public final String format(final Gang value,
            final ReportParameters reportParameters) {
        return String.format("%s: %d", getLocalizationService()
                .getReportString(ReportBundleConf.BULLETS), value.getBullets());
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
