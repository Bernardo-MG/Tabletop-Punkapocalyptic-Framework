package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;

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
        final ComponentBuilder<?, ?> actions;
        final ComponentBuilder<?, ?> combat;
        final ComponentBuilder<?, ?> precision;
        final ComponentBuilder<?, ?> agility;
        final ComponentBuilder<?, ?> strength;
        final ComponentBuilder<?, ?> toughness;
        final ComponentBuilder<?, ?> tech;

        actions = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.ACTIONS_SHORT), ReportConf.ACTIONS);
        combat = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.COMBAT_SHORT), ReportConf.COMBAT);
        precision = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.PRECISION_SHORT), ReportConf.PRECISION);
        agility = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.AGILITY_SHORT), ReportConf.AGILITY);
        strength = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.STRENGTH_SHORT), ReportConf.STRENGTH);
        toughness = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.TOUGHNESS_SHORT), ReportConf.TOUGHNESS);
        tech = getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.TECH_SHORT), ReportConf.TECH);

        return Components.horizontalList(actions, combat, precision, agility,
                strength, toughness, tech);
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final ComponentBuilder<?, ?> getAttributeCell(final String label,
            final String field) {
        final TextFieldBuilder<String> labelText;
        final TextFieldBuilder<Integer> labelValue;

        labelText = Components.text(label);
        labelValue = Components.text(DynamicReportsFactory.getInstance()
                .getIntegerField(field));

        return DynamicReportsFactory.getInstance().getBorderedCellComponent(
                Components.horizontalList(labelText, labelValue));
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
