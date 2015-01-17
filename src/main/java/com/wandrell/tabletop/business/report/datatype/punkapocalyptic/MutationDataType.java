package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.mutation.Mutation;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class MutationDataType extends
        AbstractDataType<Mutation, Mutation> {

    private static final long                         serialVersionUID = 1L;
    private final DRIValueFormatter<String, Mutation> formatter        = new MutationFormatter();
    private final LocalizationService                 service;

    private class MutationFormatter extends
            AbstractValueFormatter<String, Mutation> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Mutation value, ReportParameters reportParameters) {
            return getLocalizationService().getMutationString(value.getName());
        }

    }

    public MutationDataType(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Mutation> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String
            valueToString(final Mutation value, final Locale locale) {
        return value.getName();
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
