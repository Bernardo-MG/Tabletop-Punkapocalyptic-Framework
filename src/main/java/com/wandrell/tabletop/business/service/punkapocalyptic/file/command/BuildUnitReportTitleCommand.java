package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;

import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.UnitDataType;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.GroupedUnitNameFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.UnitValorationFormatter;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

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
