package com.wandrell.tabletop.business.report.formatter.punkapocalyptic;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.punkapocalyptic.ArmorUtils;

public final class ArmorArmorFormatter extends
        AbstractValueFormatter<String, Armor> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public ArmorArmorFormatter() {
        super();
    }

    @Override
    public final String format(final Armor value,
            final ReportParameters reportParameters) {
        return ArmorUtils.getArmor(value);
    }

}
