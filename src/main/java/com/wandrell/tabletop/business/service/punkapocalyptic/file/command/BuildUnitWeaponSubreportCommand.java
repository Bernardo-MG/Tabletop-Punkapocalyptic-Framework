package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.Expressions;
import net.sf.dynamicreports.report.constant.WhenNoDataType;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.SpecialRulesDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.WeaponDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.WeaponEnhancementDataType;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponCombatFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponDistanceImperialFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponDistanceMetricFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponNameFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponPenetrationFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponStrengthFormatter;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;

public final class BuildUnitWeaponSubreportCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, LocalizationServiceAware {

    private LocalizationService localizationService;

    public BuildUnitWeaponSubreportCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final SubreportBuilder subreport;

        subreport = createWeaponsSubreport();
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPONS));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(subreport);
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final SubreportBuilder createWeaponsSubreport() {
        final JasperReportBuilder report;

        report = DynamicReports.report();
        report.detail(DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(getWeaponDetailComponent()));

        return Components.subreport(report);
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final SubreportBuilder getRulesSubreport() {
        final JasperReportBuilder report;
        final DRField<SpecialRule> field;

        field = new DRField<SpecialRule>(ReportConf.CURRENT, SpecialRule.class);
        field.setDataType(new SpecialRulesDataType(getLocalizationService()));

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(getLocalizationService().getReportString(
                ReportBundleConf.RULES)));
        report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

        return Components.subreport(report);
    }

    private final DRField<Weapon> getWeaponCombatField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponCombatFormatter()));

        return field;
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent() {
        final ComponentBuilder<?, ?> data;
        final ComponentBuilder<?, ?> attributes;
        final SubreportBuilder rules;
        final SubreportBuilder equipment;

        rules = getRulesSubreport();
        rules.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES));
        rules.setHeight(10);

        equipment = getWeaponEnhancementsSubreport();
        equipment.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPON_ENHANCEMENTS));
        equipment.setHeight(10);

        attributes = Components
                .verticalList(
                        Components.text(getWeaponNameField(ReportConf.CURRENT,
                                getLocalizationService())),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(getLocalizationService()
                                        .getReportString(
                                                ReportBundleConf.COMBAT)),
                                Components
                                        .text(getWeaponCombatField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(getLocalizationService()
                                        .getReportString(
                                                ReportBundleConf.STRENGTH)),
                                Components
                                        .text(getWeaponStrengthField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(getLocalizationService()
                                        .getReportString(
                                                ReportBundleConf.PENETRATION)),
                                Components
                                        .text(getWeaponPenetrationField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components
                                        .text(getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.DISTANCE_METRIC)),
                                Components
                                        .text(getWeaponDistanceMetricField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components
                                        .text(getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.DISTANCE_IMPERIAL)),
                                Components
                                        .text(getWeaponDistanceImperialField(ReportConf.CURRENT))));

        data = Components.horizontalList(
                attributes,
                DynamicReportsFactory.getInstance()
                        .getBorderedCellComponentThin(
                                Components.verticalList(
                                        rules,
                                        DynamicReportsFactory.getInstance()
                                                .getBorderedCellComponentThin(
                                                        equipment))));

        return data;
    }

    private final DRField<Weapon> getWeaponDistanceImperialField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(
                new WeaponDistanceImperialFormatter()));

        return field;
    }

    private final DRField<Weapon> getWeaponDistanceMetricField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(
                new WeaponDistanceMetricFormatter()));

        return field;
    }

    private final SubreportBuilder getWeaponEnhancementsSubreport() {
        final JasperReportBuilder report;
        final DRField<WeaponEnhancement> field;

        field = new DRField<WeaponEnhancement>(ReportConf.CURRENT,
                WeaponEnhancement.class);
        field.setDataType(new WeaponEnhancementDataType(
                getLocalizationService()));

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(getLocalizationService().getReportString(
                ReportBundleConf.ENHANCEMENTS)));
        report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

        return Components.subreport(report);
    }

    private final DRField<Weapon> getWeaponNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponNameFormatter(service)));

        return field;
    }

    private final DRField<Weapon> getWeaponPenetrationField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponPenetrationFormatter()));

        return field;
    }

    private final DRField<Weapon>
            getWeaponStrengthField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponStrengthFormatter()));

        return field;
    }

}
