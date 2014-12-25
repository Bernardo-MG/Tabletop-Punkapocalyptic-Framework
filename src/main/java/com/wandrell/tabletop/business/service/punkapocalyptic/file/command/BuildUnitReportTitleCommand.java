package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;

import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.UnitDataType;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.GroupedUnitFormatter;
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
        final ComponentBuilder<?, ?> valoration;
        final DynamicReportsFactory factory;
        final TextFieldBuilder<String> unitValorationLabelText;
        final TextFieldBuilder<ValueBox> unitValorationValueText;

        factory = DynamicReportsFactory.getInstance();

        // Unit valoration fields
        unitValorationLabelText = Components.text(getLocalizationService()
                .getReportString(ReportBundleConf.VALORATION));
        unitValorationValueText = Components.text(factory
                .getValueBoxField(ReportConf.VALORATION));

        // Unit name
        unitName = factory.getBorderedCellComponent(Components
                .text(getUnitNameField(ReportConf.CURRENT,
                        getLocalizationService())));
        // Unit valoration
        valoration = factory.getBorderedCellComponent(Components
                .horizontalList(unitValorationLabelText,
                        unitValorationValueText));

        return Components.horizontalList(unitName, valoration);
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
        field.setDataType(new UnitDataType(new GroupedUnitFormatter(service)));

        return field;
    }

}
