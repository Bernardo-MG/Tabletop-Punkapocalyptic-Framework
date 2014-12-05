package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class SpecialRulesDataType extends
        AbstractDataType<SpecialRule, SpecialRule> {

    private static final long                            serialVersionUID = 1L;
    private final DRIValueFormatter<String, SpecialRule> formatter        = new SpecialRuleFormatter();
    private final LocalizationService                    service;

    private class SpecialRuleFormatter extends
            AbstractValueFormatter<String, SpecialRule> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(SpecialRule value,
                ReportParameters reportParameters) {
            return getLocalizationService().getSpecialRuleString(
                    value.getName());
        }

    }

    public SpecialRulesDataType(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, SpecialRule> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final SpecialRule value,
            final Locale locale) {
        return value.getName();
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
