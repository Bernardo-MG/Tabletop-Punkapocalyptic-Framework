package com.wandrell.tabletop.business.report.punkapocalyptic.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;

public final class ArmorDataType extends AbstractDataType<Armor, Armor> {

    private static final long                      serialVersionUID = 1L;
    private final DRIValueFormatter<String, Armor> formatter;

    public ArmorDataType(final DRIValueFormatter<String, Armor> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Armor> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Armor value, final Locale locale) {
        return value.getName();
    }

}
