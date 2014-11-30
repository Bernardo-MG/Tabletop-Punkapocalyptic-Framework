package com.wandrell.tabletop.business.report.punkapocalyptic.datatype;

import java.util.Locale;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;

public final class FactionDataType extends AbstractDataType<Faction, Faction> {

    private static final long                        serialVersionUID = 1L;
    private final DRIValueFormatter<String, Faction> formatter        = new FactionFormatter();

    private class FactionFormatter extends
            AbstractValueFormatter<String, Faction> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Faction value, ReportParameters reportParameters) {
            return value.getName();
        }
    }

    public FactionDataType() {
        super();
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Faction> getValueFormatter() {
        return formatter;
    }

    @Override
    public final String valueToString(final Faction value, final Locale locale) {
        return value.getName();
    }

}
