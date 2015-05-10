package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;

public final class WeaponEnhancementDataType extends
        AbstractDataType<WeaponEnhancement, WeaponEnhancement> {

    private static final long                                  serialVersionUID = 1L;
    private final DRIValueFormatter<String, WeaponEnhancement> formatter        = new WeaponEnhancementFormatter();

    private class WeaponEnhancementFormatter extends
            AbstractValueFormatter<String, WeaponEnhancement> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(WeaponEnhancement value,
                ReportParameters reportParameters) {
            return value.getNameToken();
        }

    }

    public WeaponEnhancementDataType() {
        super();
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, WeaponEnhancement>
            getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final WeaponEnhancement value,
            final Locale locale) {
        return value.getNameToken();
    }

}
