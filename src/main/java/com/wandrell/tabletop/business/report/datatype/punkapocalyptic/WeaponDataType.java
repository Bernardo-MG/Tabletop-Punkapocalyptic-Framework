package com.wandrell.tabletop.business.report.datatype.punkapocalyptic;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;

public final class WeaponDataType extends AbstractDataType<Weapon, Weapon> {

    private static final long                       serialVersionUID = 1L;
    private final DRIValueFormatter<String, Weapon> formatter;

    public WeaponDataType(final DRIValueFormatter<String, Weapon> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Weapon> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Weapon value, final Locale locale) {
        return String.valueOf(value.getName());
    }

}
