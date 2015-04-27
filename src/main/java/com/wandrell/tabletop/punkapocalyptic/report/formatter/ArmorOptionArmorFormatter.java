package com.wandrell.tabletop.punkapocalyptic.report.formatter;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.wandrell.tabletop.punkapocalyptic.conf.ReportBundleConf;
import com.wandrell.tabletop.punkapocalyptic.model.availability.option.ArmorOption;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.util.ArmorUtils;

public final class ArmorOptionArmorFormatter extends
        AbstractValueFormatter<String, ArmorOption> {

    private static final long         serialVersionUID = Constants.SERIAL_VERSION_UID;
    private final LocalizationService localizationService;

    public ArmorOptionArmorFormatter(final LocalizationService service) {
        super();

        localizationService = service;
    }

    @Override
    public final String format(final ArmorOption value,
            final ReportParameters reportParameters) {
        return String.format("%s: %s", getLocalizationService()
                .getReportString(ReportBundleConf.ARMOR_ARMOR), ArmorUtils
                .getArmor(value.getArmor()));
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
