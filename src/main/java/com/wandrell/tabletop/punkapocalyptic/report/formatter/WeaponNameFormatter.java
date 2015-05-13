package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitWeapon;

public final class WeaponNameFormatter extends
        AbstractValueFormatter<String, UnitWeapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponNameFormatter() {
        super();
    }

    @Override
    public final String format(final UnitWeapon value,
            final ReportParameters reportParameters) {
        return value.getWeaponTemplate().getNameToken();
    }

}
