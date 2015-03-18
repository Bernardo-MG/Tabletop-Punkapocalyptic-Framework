package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;

public final class WeaponNameFormatter extends
        AbstractValueFormatter<String, Weapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponNameFormatter() {
        super();
    }

    @Override
    public final String format(final Weapon value,
            final ReportParameters reportParameters) {
        return value.getName();
    }

}
