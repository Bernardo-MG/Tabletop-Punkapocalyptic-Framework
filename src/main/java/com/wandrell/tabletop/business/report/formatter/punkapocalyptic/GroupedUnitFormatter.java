package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.GroupedUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class GroupedUnitFormatter extends
        AbstractValueFormatter<String, Unit> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public GroupedUnitFormatter(final LocalizationService service) {
        super();

        localizationService = service;
    }

    @Override
    public final String format(final Unit value,
            final ReportParameters reportParameters) {
        final String name;
        final String result;

        name = getLocalizationService().getUnitNameString(value.getUnitName());
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
