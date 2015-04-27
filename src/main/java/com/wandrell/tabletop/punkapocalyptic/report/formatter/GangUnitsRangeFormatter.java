package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.conf.ReportBundleConf;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

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
                getRulesetService().getMaxAllowedUnits(value.getValoration()));
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

}
