package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;

import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class BuildUnitAttributesSubreportCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, LocalizationServiceAware {

    private LocalizationService localizationService;

    public BuildUnitAttributesSubreportCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final HorizontalListBuilder list;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        list = Components.horizontalList();
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.ACTIONS))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.COMBAT))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.PRECISION))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.AGILITY))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.STRENGTH))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.TOUGHNESS))));
        list.add(factory.getBorderedCellComponent(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.TECH))));

        list.newRow();
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.ACTIONS))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.COMBAT))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.PRECISION))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.AGILITY))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.STRENGTH))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.TOUGHNESS))));
        list.add(factory.getBorderedCellComponent(Components
                .text(DynamicReportsFactory.getInstance().getValueBoxField(
                        ReportConf.TECH))));

        return list;
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
