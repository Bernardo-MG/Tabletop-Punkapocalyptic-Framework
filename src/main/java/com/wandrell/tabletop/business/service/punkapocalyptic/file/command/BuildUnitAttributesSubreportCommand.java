package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;

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
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.ACTIONS)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.COMBAT)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.PRECISION)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.AGILITY)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.STRENGTH)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                getLocalizationService().getReportString(
                        ReportBundleConf.TOUGHNESS)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components
                .text(getLocalizationService().getReportString(
                        ReportBundleConf.TECH)).setHorizontalAlignment(
                        HorizontalAlignment.CENTER)));

        list.newRow();
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.ACTIONS)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.COMBAT)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.PRECISION)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.AGILITY)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.STRENGTH)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.TOUGHNESS)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                DynamicReportsFactory.getInstance().getIntegerField(
                        ReportConf.TECH)).setHorizontalAlignment(
                HorizontalAlignment.CENTER)));

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
