package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class WeaponNameFormatter extends
        AbstractValueFormatter<String, Weapon> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService service;

    public WeaponNameFormatter(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String format(final Weapon value,
            final ReportParameters reportParameters) {
        return getLocalizationService().getWeaponString(value.getName());
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
