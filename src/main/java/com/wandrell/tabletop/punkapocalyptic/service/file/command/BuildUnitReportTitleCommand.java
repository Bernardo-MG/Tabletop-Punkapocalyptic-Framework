package com.wandrell.tabletop.punkapocalyptic.service.file.command;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.conf.ReportConf;
import com.wandrell.tabletop.punkapocalyptic.conf.factory.DynamicReportsFactory;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.UnitDataType;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GroupedUnitNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.UnitValorationFormatter;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.LocalizationServiceAware;

public final class BuildUnitReportTitleCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, LocalizationServiceAware {

    private LocalizationService localizationService;

    public BuildUnitReportTitleCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final ComponentBuilder<?, ?> unitName;
        final ComponentBuilder<?, ?> unitValoration;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        // Unit valoration fields
        unitValoration = Components.text(getUnitValorationField(
                ReportConf.CURRENT, getLocalizationService()));

        // Unit name
        unitName = Components.text(getUnitNameField(ReportConf.CURRENT,
                getLocalizationService()));

        return factory.getBorderedCellComponentThin(Components.verticalList(
                unitName, unitValoration));
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final DRField<Unit> getUnitNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Unit> field;

        field = new DRField<Unit>(fieldName, Unit.class);
        field.setDataType(new UnitDataType(
                new GroupedUnitNameFormatter(service)));

        return field;
    }

    private final DRField<Unit> getUnitValorationField(final String fieldName,
            final LocalizationService service) {
        final DRField<Unit> field;

        field = new DRField<Unit>(fieldName, Unit.class);
        field.setDataType(new UnitDataType(new UnitValorationFormatter(service)));

        return field;
    }

}
