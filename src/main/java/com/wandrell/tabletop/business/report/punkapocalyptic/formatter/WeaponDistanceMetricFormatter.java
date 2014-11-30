package com.wandrell.tabletop.business.report.punkapocalyptic.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.punkapocalyptic.WeaponUtils;

public final class WeaponDistanceMetricFormatter extends
        AbstractValueFormatter<String, Weapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponDistanceMetricFormatter() {
        super();
    }

    @Override
    public String format(final Weapon value,
            final ReportParameters reportParameters) {
        final String result;

        if (value instanceof MeleeWeapon) {
            result = "-";
        } else {
            result = WeaponUtils
                    .getRangedWeaponDistanceMetric((RangedWeapon) value);
        }

        return result;
    }

}
