package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;

public final class GroupedUnitNameFormatter extends
        AbstractValueFormatter<String, Unit> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public GroupedUnitNameFormatter(final LocalizationService service) {
        super();

        localizationService = service;
    }

    @Override
    public final String format(final Unit value,
            final ReportParameters reportParameters) {
        final String name;
        final String result;

        name = getLocalizationService().getUnitNameString(value.getName());
        if (value instanceof GroupedUnit) {
            result = String.format("%dx %s", ((GroupedUnit) value)
                    .getGroupSize().getValue(), name);
        } else {
            result = name;
        }

        return result;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
