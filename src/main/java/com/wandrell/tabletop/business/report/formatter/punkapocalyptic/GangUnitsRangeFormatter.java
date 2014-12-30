package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;

public final class GangUnitsRangeFormatter extends
        AbstractValueFormatter<String, Gang> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;
    private final RulesetService      rulesetService;

    public GangUnitsRangeFormatter(
            final LocalizationService localizationService,
            final RulesetService rulesetService) {
        super();

        this.rulesetService = rulesetService;
        this.localizationService = localizationService;
    }

    @Override
    public final String format(final Gang value,
            final ReportParameters reportParameters) {
        return String.format(
                getLocalizationService().getReportString(
                        ReportBundleConf.UNITS_RANGE), value.getUnits().size(),
                getRulesetService().getMaxAllowedUnits(value));
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

}
