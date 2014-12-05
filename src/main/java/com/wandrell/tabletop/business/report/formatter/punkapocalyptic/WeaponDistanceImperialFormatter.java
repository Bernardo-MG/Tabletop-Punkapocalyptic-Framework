package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.punkapocalyptic.WeaponUtils;

public class WeaponDistanceImperialFormatter extends
        AbstractValueFormatter<String, Weapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponDistanceImperialFormatter() {
        super();
    }

    @Override
    public final String format(final Weapon value,
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
