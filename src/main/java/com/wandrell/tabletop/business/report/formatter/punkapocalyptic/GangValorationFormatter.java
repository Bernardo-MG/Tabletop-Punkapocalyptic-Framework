package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class GangValorationFormatter extends
        AbstractValueFormatter<String, ValueBox> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public GangValorationFormatter(final LocalizationService localizationService) {
        super();

        this.localizationService = localizationService;
    }

    @Override
    public final String format(final ValueBox value,
            final ReportParameters reportParameters) {
        return String.format("%s: %d", getLocalizationService()
                .getReportString(ReportBundleConf.VALORATION_TOTAL), value
                .getValue());
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
