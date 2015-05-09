package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitWeapon;

public final class WeaponCombatFormatter extends
        AbstractValueFormatter<String, UnitWeapon> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public WeaponCombatFormatter() {
        super();
    }

    @Override
    public String format(final UnitWeapon value,
            final ReportParameters reportParameters) {
        final String result;

        if (value instanceof MeleeWeapon) {
            result = String.valueOf(((MeleeWeapon) value).getCombat());
        } else {
            result = "-";
        }

        return result;
    }

}
