package com.wandrell.tabletop.business.report.punkapocalyptic.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class EquipmentDataType extends
        AbstractDataType<Equipment, Equipment> {

    private static final long                          serialVersionUID = 1L;
    private final DRIValueFormatter<String, Equipment> formatter        = new EquipmentFormatter();
    private final LocalizationService                  service;

    private class EquipmentFormatter extends
            AbstractValueFormatter<String, Equipment> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String
                format(Equipment value, ReportParameters reportParameters) {
            return getLocalizationService().getEquipmentString(value.getName());
        }

    }

    public EquipmentDataType(final LocalizationService service) {
        super();

        this.service = service;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Equipment> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String
            valueToString(final Equipment value, final Locale locale) {
        return value.getName();
    }

    private final LocalizationService getLocalizationService() {
        return service;
    }

}
