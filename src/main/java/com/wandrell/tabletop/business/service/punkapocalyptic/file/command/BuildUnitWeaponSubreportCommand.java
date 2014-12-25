package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.Expressions;

import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
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
import com.wandrell.util.command.ReturnCommand;

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

        return DynamicReportsFactory.getInstance().getBorderedCellComponent(
                subreport);
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final SubreportBuilder createWeaponsSubreport() {
        final JasperReportBuilder report;

        report = DynamicReports.report();
        report.detail(DynamicReportsFactory.getInstance()
                .getBorderedCellComponent(getWeaponDetailComponent()));

        return Components.subreport(report);
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final DRField<Weapon> getWeaponCombatField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponCombatFormatter()));

        return field;
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent() {
        final ComponentBuilder<?, ?> data;
        final SubreportBuilder rules;
        final SubreportBuilder equipment;

        rules = DynamicReportsFactory.getInstance().getRulesSubreport(
                getLocalizationService()
                        .getReportString(ReportBundleConf.RULES),
                getLocalizationService());
        rules.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES));

        equipment = getWeaponEnhancementsSubreport(getLocalizationService()
                .getReportString(ReportBundleConf.ENHANCEMENTS),
                getLocalizationService());
        equipment.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPON_ENHANCEMENTS));

        data = Components
                .verticalList(
                        Components.text(getWeaponNameField(ReportConf.CURRENT,
                                getLocalizationService())),
                        Components.horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        ReportBundleConf.COMBAT)), Components
                                .text(getWeaponCombatField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.text(getLocalizationService()
                                        .getReportString(
                                                ReportBundleConf.STRENGTH)),
                                Components
                                        .text(getWeaponStrengthField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.text(getLocalizationService()
                                        .getReportString(
                                                ReportBundleConf.PENETRATION)),
                                Components
                                        .text(getWeaponPenetrationField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components
                                        .text(getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.DISTANCE_METRIC)),
                                Components
                                        .text(getWeaponDistanceMetricField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components
                                        .text(getLocalizationService()
                                                .getReportString(
                                                        ReportBundleConf.DISTANCE_IMPERIAL)),
                                Components
                                        .text(getWeaponDistanceImperialField(ReportConf.CURRENT))),
                        DynamicReportsFactory.getInstance()
                                .getBorderedCellComponent(equipment),
                        DynamicReportsFactory.getInstance()
                                .getBorderedCellComponent(rules));

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

    private final SubreportBuilder getWeaponEnhancementsSubreport(
            final String column, final LocalizationService service) {
        JasperReportBuilder report;

        report = DynamicReports.report();
        report.columns(DynamicReports.col.column(column, "_THIS",
                new WeaponEnhancementDataType(service)));

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
