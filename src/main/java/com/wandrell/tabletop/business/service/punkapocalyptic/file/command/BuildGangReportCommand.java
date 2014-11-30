package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractSubDatasourceExpression;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.ReportStylesFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class BuildGangReportCommand implements
        ReturnCommand<JasperReportBuilder>, ApplicationInfoServiceAware,
        LocalizationServiceAware {

    private ApplicationInfoService    appInfoService;
    private final ReportStylesFactory factory = ReportStylesFactory
                                                      .getInstance();
    private LocalizationService       localizationService;
    private final InputStream         path;

    private class CurrentUnitDatasourceExpression extends
            AbstractSubDatasourceExpression<Unit> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        public CurrentUnitDatasourceExpression(
                DRIExpression<? extends Unit> expression) {
            super(expression);
        }

        public CurrentUnitDatasourceExpression(String fieldName) {
            super(fieldName);
        }

        @Override
        protected JRDataSource createSubDatasource(Unit data) {
            final Collection<Unit> list;
            list = new LinkedList<>();
            list.add(data);
            return new JRBeanCollectionDataSource(list);
        }
    }

    public BuildGangReportCommand(final InputStream path) {
        super();

        this.path = path;
    }

    @Override
    public final JasperReportBuilder execute() throws DRException {
        JasperReportBuilder report;
        SubreportBuilder subreport;

        report = DynamicReports.report();
        subreport = createUnitListSubreport(DynamicReports.exp
                .subDatasourceBeanCollection("units"));

        report.setTemplate(factory.getReportTemplate());
        report.title(createTitleComponent());
        report.pageFooter(factory.getReportFooter());
        report.detailFooter(subreport);

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
        return cmp
                .horizontalList()
                .add(factory
                        .getTitleLabelComponent(getImage(),
                                getApplicationInfoService()
                                        .getApplicationName(),
                                getApplicationInfoService().getDownloadURI()
                                        .toString()))
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.text(getLocalizationService().getReportString(
                        "faction")))
                .add(cmp.text(factory.getFactionField("faction")))
                .newRow()
                .add(cmp.text(getLocalizationService().getReportString(
                        "valoration")))
                .add(cmp.text(factory.getValueHandlerValueField("valoration")))
                .newRow()
                .add(cmp.text(getLocalizationService().getReportString(
                        "bullets")))
                .add(cmp.text(factory.getValueHandlerValueField("bullets")))
                .newRow()
                .add(cmp.text(getLocalizationService().getReportString("units")))
                .add(cmp.text(factory.getCollectionSizeField("units")))
                .newRow().add(cmp.line()).newRow().add(cmp.verticalGap(10));
    }

    private final SubreportBuilder createUnitListSubreport(
            DRIExpression<JRDataSource> dataSource) {
        SubreportBuilder subreport;

        subreport = cmp.subreport(createUnitSubreport()).setDataSource(
                dataSource);

        JasperReportBuilder report = DynamicReports.report();
        report.setTemplate(factory.getReportTemplate());
        report.addDetail(subreport);
        subreport.setDataSource(new CurrentUnitDatasourceExpression("_THIS"));

        return cmp.subreport(report).setDataSource(dataSource);
    }

    private final JasperReportBuilder createUnitSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.setTemplate(factory.getReportTemplate());

        report.title(getUnitTitleComponent());

        report.detail(getUnitDetailComponent());

        report.detailFooter(cmp.verticalGap(20));

        return report;
    }

    private final SubreportBuilder createWeaponsSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.detail(factory
                .getBorderedCellComponent(getWeaponDetailComponent()));

        return cmp.subreport(report);
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final ComponentBuilder<?, ?> getArmorComponent() {
        final ComponentBuilder<?, ?> armorName;
        final ComponentBuilder<?, ?> armorArmor;

        armorName = factory.getBorderedCellComponent(cmp.horizontalList(cmp
                .text(getLocalizationService().getReportString("armor.name")),
                cmp.text(factory.getArmorNameField("armor"))));
        armorArmor = factory.getBorderedCellComponent(cmp.horizontalList(cmp
                .text(getLocalizationService().getReportString("armor.armor")),
                cmp.text(factory.getArmorArmorField("armor"))));

        return cmp.horizontalList(armorName, armorArmor);
    }

    private final ComponentBuilder<?, ?> getAttributesComponent() {
        final ComponentBuilder<?, ?> actions;
        final ComponentBuilder<?, ?> combat;
        final ComponentBuilder<?, ?> precision;
        final ComponentBuilder<?, ?> agility;
        final ComponentBuilder<?, ?> strength;
        final ComponentBuilder<?, ?> toughness;
        final ComponentBuilder<?, ?> tech;

        actions = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "actions.short")),
                cmp.text(factory.getIntegerField("actions"))));
        combat = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "combat.short")),
                cmp.text(factory.getIntegerField("combat"))));
        precision = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "precision.short")),
                cmp.text(factory.getIntegerField("precision"))));
        agility = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "agility.short")),
                cmp.text(factory.getIntegerField("agility"))));
        strength = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "strength.short")),
                cmp.text(factory.getIntegerField("strength"))));
        toughness = factory.getBorderedCellComponent(cmp.horizontalList(
                cmp.text(getLocalizationService().getReportString(
                        "toughness.short")),
                cmp.text(factory.getIntegerField("toughness"))));
        tech = factory.getBorderedCellComponent(cmp.horizontalList(cmp
                .text(getLocalizationService().getReportString("tech.short")),
                cmp.text(factory.getIntegerField("tech"))));

        return cmp.horizontalList(actions, combat, precision, agility,
                strength, toughness, tech);
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

        rules = factory.getBorderedCellComponent(factory.getRulesSubreport(
                getLocalizationService().getReportString("rules"))
                .setDataSource(
                        DynamicReports.exp
                                .subDatasourceBeanCollection("specialRules")));

        attributes = getAttributesComponent();

        armor = getArmorComponent();

        equipment = factory.getBorderedCellComponent(factory
                .getEquipmentSubreport(
                        getLocalizationService().getReportString("equipment"))
                .setDataSource(
                        DynamicReports.exp
                                .subDatasourceBeanCollection("equipment")));

        weapons = factory.getBorderedCellComponent(createWeaponsSubreport()
                .setDataSource(
                        DynamicReports.exp
                                .subDatasourceBeanCollection("weapons")));

        return cmp.verticalList(attributes, rules, armor, equipment, weapons);
    }

    private final ComponentBuilder<?, ?> getUnitTitleComponent() {
        ComponentBuilder<?, ?> name;
        ComponentBuilder<?, ?> points;

        name = factory.getBorderedCellComponent(cmp.text(new DRField<String>(
                "unitName", String.class)));
        points = factory.getBorderedCellComponent(cmp.horizontalList(cmp
                .text(getLocalizationService().getReportString("valoration")),
                cmp.text(factory.getValueHandlerValueField("valoration"))));

        return cmp.horizontalList(name, points);
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent() {
        final ComponentBuilder<?, ?> data;
        final SubreportBuilder rules;

        rules = factory.getRulesSubreport(getLocalizationService()
                .getReportString("rules"));
        rules.setDataSource(DynamicReports.exp
                .subDatasourceBeanCollection("specialRules"));

        data = cmp
                .verticalList(
                        cmp.text(factory.getStringField("name")),
                        cmp.horizontalList(cmp.text(getLocalizationService()
                                .getReportString("combat")), cmp.text(factory
                                .getWeaponCombatField("_THIS"))),
                        cmp.horizontalList(cmp.text(getLocalizationService()
                                .getReportString("strength")), cmp.text(factory
                                .getWeaponStrengthField("_THIS"))),
                        cmp.horizontalList(cmp.text(getLocalizationService()
                                .getReportString("penetration")), cmp
                                .text(factory
                                        .getWeaponPenetrationField("_THIS"))),
                        cmp.horizontalList(cmp.text(getLocalizationService()
                                .getReportString("distance.metric")), cmp
                                .text(factory
                                        .getWeaponDistanceMetricField("_THIS"))),
                        cmp.horizontalList(
                                cmp.text(getLocalizationService()
                                        .getReportString("distance.imperial")),
                                cmp.text(factory
                                        .getWeaponDistanceImperialField("_THIS"))),
                        factory.getBorderedCellComponent(rules));

        return data;
    }

}
