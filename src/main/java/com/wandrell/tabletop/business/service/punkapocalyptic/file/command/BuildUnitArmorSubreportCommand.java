package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.ArmorDataType;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.ArmorArmorFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.ArmorNameFormatter;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;

public final class BuildUnitArmorSubreportCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, LocalizationServiceAware {

    private LocalizationService localizationService;

    public BuildUnitArmorSubreportCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final TextFieldBuilder<String> armorNameLabelText;
        final TextFieldBuilder<Armor> armorNameText;
        final TextFieldBuilder<Armor> armorArmorText;

        armorNameLabelText = Components.text(getLocalizationService()
                .getReportString(ReportBundleConf.ARMOR_NAME));
        armorNameText = Components.text(getArmorNameField(ReportConf.ARMOR,
                getLocalizationService()));

        armorArmorText = Components.text(getArmorArmorField(ReportConf.ARMOR));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(
                        Components
                                .horizontalList(armorNameLabelText)
                                .newRow()
                                .add(Components.horizontalList(
                                        Components.horizontalGap(10),
                                        armorNameText, armorArmorText)
                                        .setFixedWidth(300)));
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final DRField<Armor> getArmorArmorField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorArmorFormatter(
                getLocalizationService())));

        return field;
    }

    private final DRField<Armor> getArmorNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorNameFormatter(service)));

        return field;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
