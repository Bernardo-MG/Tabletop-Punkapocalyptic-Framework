package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.InputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.Expressions;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.report.expression.CurrentObjectDatasourceExpression;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class BuildGangReportCommand implements
        ReturnCommand<JasperReportBuilder>, ApplicationInfoServiceAware,
        LocalizationServiceAware {

    private ApplicationInfoService      appInfoService;
    private final DynamicReportsFactory factory = DynamicReportsFactory
                                                        .getInstance();
    private LocalizationService         localizationService;
    private final InputStream           path;

    public BuildGangReportCommand(final InputStream path) {
        super();

        this.path = path;
    }

    @Override
    public final JasperReportBuilder execute() throws DRException {
        JasperReportBuilder report;
        SubreportBuilder subreport;

        report = DynamicReports.report();
        subreport = createUnitListSubreport(Expressions
                .subDatasourceBeanCollection(ReportConf.UNITS));

        report.setTemplate(getDynamicReportsFactory().getReportTemplate());
        report.title(createTitleComponent());
        report.detailFooter(subreport);
        report.pageFooter(getDynamicReportsFactory().getReportFooter());

        return report;
    }

    @Override
    public final void setApplicationInfoService(
            final ApplicationInfoService service) {
        appInfoService = service;
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final ComponentBuilder<?, ?> createTitleComponent() {
        final ComponentBuilder<?, ?> brand;

        brand = getDynamicReportsFactory().getTitleLabelComponent(getImage(),
                getApplicationInfoService().getApplicationName(),
                getApplicationInfoService().getDownloadURI().toString());

        return Components
                .horizontalList()
                .add(brand)
                .newRow()
                .add(Components.line())
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.FACTION)))
                .add(Components.text(getDynamicReportsFactory()
                        .getFactionField(ReportConf.FACTION,
                                getLocalizationService())))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.VALORATION)))
                .add(Components.text(getDynamicReportsFactory()
                        .getValueHandlerValueField(ReportConf.VALORATION)))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.BULLETS)))
                .add(Components.text(getDynamicReportsFactory()
                        .getValueHandlerValueField(ReportConf.BULLETS)))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.UNITS)))
                .add(Components.text(getDynamicReportsFactory()
                        .getCollectionSizeField(ReportConf.UNITS))).newRow()
                .add(Components.line()).newRow()
                .add(Components.verticalGap(10));
    }

    private final SubreportBuilder createUnitListSubreport(
            DRIExpression<JRDataSource> dataSource) {
        SubreportBuilder subreport;

        subreport = Components.subreport(createUnitSubreport()).setDataSource(
                dataSource);

        JasperReportBuilder report = DynamicReports.report();
        report.setTemplate(getDynamicReportsFactory().getReportTemplate());
        report.addDetail(subreport);
        subreport.setDataSource(new CurrentObjectDatasourceExpression(
                ReportConf.CURRENT));

        return Components.subreport(report).setDataSource(dataSource);
    }

    private final JasperReportBuilder createUnitSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.setTemplate(getDynamicReportsFactory().getReportTemplate());

        report.title(getUnitTitleComponent());

        report.detail(getUnitDetailComponent());

        report.detailFooter(Components.verticalGap(20));

        return report;
    }

    private final SubreportBuilder createWeaponsSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.detail(getDynamicReportsFactory().getBorderedCellComponent(
                getWeaponDetailComponent()));

        return Components.subreport(report);
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final ComponentBuilder<?, ?> getArmorComponent() {
        final ComponentBuilder<?, ?> armorName;
        final ComponentBuilder<?, ?> armorArmor;

        armorName = getDynamicReportsFactory().getBorderedCellComponent(
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                ReportBundleConf.ARMOR_NAME)), Components
                        .text(getDynamicReportsFactory().getArmorNameField(
                                ReportConf.ARMOR, getLocalizationService()))));
        armorArmor = getDynamicReportsFactory().getBorderedCellComponent(
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                ReportBundleConf.ARMOR_ARMOR)), Components
                        .text(getDynamicReportsFactory().getArmorArmorField(
                                ReportConf.ARMOR))));

        return Components.horizontalList(armorName, armorArmor);
    }

    private final ComponentBuilder<?, ?> getAttributesComponent() {
        final ComponentBuilder<?, ?> actions;
        final ComponentBuilder<?, ?> combat;
        final ComponentBuilder<?, ?> precision;
        final ComponentBuilder<?, ?> agility;
        final ComponentBuilder<?, ?> strength;
        final ComponentBuilder<?, ?> toughness;
        final ComponentBuilder<?, ?> tech;

        actions = getDynamicReportsFactory().getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.ACTIONS_SHORT), ReportConf.ACTIONS);
        combat = getDynamicReportsFactory().getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.COMBAT_SHORT), ReportConf.COMBAT);
        precision = getDynamicReportsFactory()
                .getAttributeCell(
                        getLocalizationService().getReportString(
                                ReportBundleConf.PRECISION_SHORT),
                        ReportConf.PRECISION);
        agility = getDynamicReportsFactory().getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.AGILITY_SHORT), ReportConf.AGILITY);
        strength = getDynamicReportsFactory().getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.STRENGTH_SHORT), ReportConf.STRENGTH);
        toughness = getDynamicReportsFactory()
                .getAttributeCell(
                        getLocalizationService().getReportString(
                                ReportBundleConf.TOUGHNESS_SHORT),
                        ReportConf.TOUGHNESS);
        tech = getDynamicReportsFactory().getAttributeCell(
                getLocalizationService().getReportString(
                        ReportBundleConf.TECH_SHORT), ReportConf.TECH);

        return Components.horizontalList(actions, combat, precision, agility,
                strength, toughness, tech);
    }

    private final DynamicReportsFactory getDynamicReportsFactory() {
        return factory;
    }

    private final InputStream getImage() {
        return path;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final ComponentBuilder<?, ?> getUnitDetailComponent() {
        final ComponentBuilder<?, ?> rules;
        final ComponentBuilder<?, ?> attributes;
        final ComponentBuilder<?, ?> armor;
        final ComponentBuilder<?, ?> equipment;
        final ComponentBuilder<?, ?> weapons;

        rules = getDynamicReportsFactory()
                .getBorderedCellComponent(
                        getDynamicReportsFactory()
                                .getRulesSubreport(
                                        getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.RULES),
                                        getLocalizationService())
                                .setDataSource(
                                        Expressions
                                                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES)));

        attributes = getAttributesComponent();

        armor = getArmorComponent();

        equipment = getDynamicReportsFactory()
                .getBorderedCellComponent(
                        getDynamicReportsFactory()
                                .getEquipmentSubreport(
                                        getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.EQUIPMENT),
                                        getLocalizationService())
                                .setDataSource(
                                        Expressions
                                                .subDatasourceBeanCollection(ReportConf.EQUIPMENT)));

        weapons = getDynamicReportsFactory()
                .getBorderedCellComponent(
                        createWeaponsSubreport()
                                .setDataSource(
                                        Expressions
                                                .subDatasourceBeanCollection(ReportConf.WEAPONS)));

        return Components.verticalList(attributes, rules, armor, equipment,
                weapons);
    }

    private final ComponentBuilder<?, ?> getUnitTitleComponent() {
        ComponentBuilder<?, ?> name;
        ComponentBuilder<?, ?> points;

        name = getDynamicReportsFactory().getBorderedCellComponent(
                Components.text(getDynamicReportsFactory().getUnitNameField(
                        ReportConf.CURRENT, getLocalizationService())));
        points = getDynamicReportsFactory().getBorderedCellComponent(
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                ReportBundleConf.VALORATION)), Components
                        .text(getDynamicReportsFactory()
                                .getValueHandlerValueField(
                                        ReportConf.VALORATION))));

        return Components.horizontalList(name, points);
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent() {
        final ComponentBuilder<?, ?> data;
        final SubreportBuilder rules;
        final SubreportBuilder equipment;

        rules = getDynamicReportsFactory().getRulesSubreport(
                getLocalizationService()
                        .getReportString(ReportBundleConf.RULES),
                getLocalizationService());
        rules.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES));

        equipment = getDynamicReportsFactory().getWeaponEnhancementsSubreport(
                getLocalizationService().getReportString(
                        ReportBundleConf.ENHANCEMENTS),
                getLocalizationService());
        equipment.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPON_ENHANCEMENTS));

        data = Components
                .verticalList(
                        Components.text(getDynamicReportsFactory()
                                .getWeaponNameField(ReportConf.CURRENT,
                                        getLocalizationService())),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.COMBAT)), Components
                                .text(getDynamicReportsFactory()
                                        .getWeaponCombatField(
                                                ReportConf.CURRENT))),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.STRENGTH)), Components
                                .text(getDynamicReportsFactory()
                                        .getWeaponStrengthField(
                                                ReportConf.CURRENT))),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.PENETRATION)),
                                Components.text(getDynamicReportsFactory()
                                        .getWeaponPenetrationField(
                                                ReportConf.CURRENT))),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.DISTANCE_METRIC)),
                                Components.text(getDynamicReportsFactory()
                                        .getWeaponDistanceMetricField(
                                                ReportConf.CURRENT))),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.DISTANCE_IMPERIAL)),
                                Components.text(getDynamicReportsFactory()
                                        .getWeaponDistanceImperialField(
                                                ReportConf.CURRENT))),
                        getDynamicReportsFactory().getBorderedCellComponent(
                                equipment), getDynamicReportsFactory()
                                .getBorderedCellComponent(rules));

        return data;
    }

}
