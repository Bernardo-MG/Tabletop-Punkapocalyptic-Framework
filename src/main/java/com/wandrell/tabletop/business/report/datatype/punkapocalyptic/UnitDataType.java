package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class UnitDataType extends AbstractDataType<Unit, Unit> {

    private static final long                     serialVersionUID = 1L;
    private final DRIValueFormatter<String, Unit> formatter        = new UnitFormatter();
    private final LocalizationService             service;

    private class UnitFormatter extends AbstractValueFormatter<String, Unit> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Unit value, ReportParameters reportParameters) {
            return getLocalizationService().getUnitNameString(
                    value.getUnitName());
        }

    }

    public UnitDataType(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Unit> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Unit value, final Locale locale) {
        return value.getUnitName();
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
