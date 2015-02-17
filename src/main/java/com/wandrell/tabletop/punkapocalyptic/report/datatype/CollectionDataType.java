package com.wandrell.tabletop.punkapocalyptic.report.datatype;

import java.util.Collection;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;

public final class CollectionDataType extends
        AbstractDataType<Collection<?>, Collection<?>> {

    private static final long                              serialVersionUID = 1L;
    private final DRIValueFormatter<String, Collection<?>> formatter;

    public CollectionDataType(
            final DRIValueFormatter<String, Collection<?>> formatter) {
        super();

        this.formatter = formatter;
    }

    @Override
    public final String getPattern() {
        return Defaults.getDefaults().getStringType().getPattern();
    }

    @Override
    public final DRIValueFormatter<String, Collection<?>> getValueFormatter() {
        return formatter;
    }

}
