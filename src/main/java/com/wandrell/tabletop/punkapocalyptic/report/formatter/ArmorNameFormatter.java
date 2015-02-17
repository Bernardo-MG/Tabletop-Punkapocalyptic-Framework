package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;

public final class ArmorNameFormatter extends
        AbstractValueFormatter<String, Armor> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService service;

    public ArmorNameFormatter(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String format(final Armor value,
            final ReportParameters reportParameters) {
        return getLocalizationService().getArmorString(value.getName());
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }
}
