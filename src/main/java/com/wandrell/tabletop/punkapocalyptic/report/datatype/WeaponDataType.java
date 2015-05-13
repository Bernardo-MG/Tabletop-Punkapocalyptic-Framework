package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.UnitWeapon;

public final class WeaponDataType extends
        AbstractDataType<UnitWeapon, UnitWeapon> {

    private static final long                           serialVersionUID = 1L;
    private final DRIValueFormatter<String, UnitWeapon> formatter;

    public WeaponDataType(final DRIValueFormatter<String, UnitWeapon> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, UnitWeapon> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final UnitWeapon value,
            final Locale locale) {
        return String.valueOf(value.getWeaponTemplate().getNameToken());
    }

}
