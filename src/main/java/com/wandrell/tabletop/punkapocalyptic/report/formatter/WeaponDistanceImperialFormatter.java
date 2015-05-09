package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.RangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitWeapon;
import com.wandrell.tabletop.punkapocalyptic.util.WeaponUtils;

public class WeaponDistanceImperialFormatter extends
        AbstractValueFormatter<String, UnitWeapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponDistanceImperialFormatter() {
        super();
    }

    @Override
    public final String format(final UnitWeapon value,
            final ReportParameters reportParameters) {
        final String result;

        if (value instanceof MeleeWeapon) {
            result = "-";
        } else {
            result = WeaponUtils
                    .getRangedWeaponDistanceImperial((RangedWeapon) value);
        }

        return result;
    }

}
