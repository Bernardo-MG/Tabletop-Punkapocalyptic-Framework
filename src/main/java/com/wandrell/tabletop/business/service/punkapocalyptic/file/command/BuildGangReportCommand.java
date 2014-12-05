package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractSubDatasourceExpression;
import net.sf.dynamicreports.report.builder.expression.Expressions;
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
        subreport = createUnitListSubreport(Expressions
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
        return Components
                .horizontalList()
                .add(factory
                        .getTitleLabelComponent(getImage(),
                                getApplicationInfoService()
                                        .getApplicationName(),
                                getApplicationInfoService().getDownloadURI()
                                        .toString()))
                .newRow()
                .add(Components.line())
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        "faction")))
                .add(Components.text(factory.getFactionField("faction",
                        getLocalizationService())))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        "valoration")))
                .add(Components.text(factory
                        .getValueHandlerValueField("valoration")))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        "bullets")))
                .add(Components.text(factory
                        .getValueHandlerValueField("bullets")))
                .newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        "units")))
                .add(Components.text(factory.getCollectionSizeField("units")))
                .newRow().add(Components.line()).newRow()
                .add(Components.verticalGap(10));
    }

    private final SubreportBuilder createUnitListSubreport(
            DRIExpression<JRDataSource> dataSource) {
        SubreportBuilder subreport;

        subreport = Components.subreport(createUnitSubreport()).setDataSource(
                dataSource);

        JasperReportBuilder report = DynamicReports.report();
        report.setTemplate(factory.getReportTemplate());
        report.addDetail(subreport);
        subreport.setDataSource(new CurrentUnitDatasourceExpression("_THIS"));

        return Components.subreport(report).setDataSource(dataSource);
    }

    private final JasperReportBuilder createUnitSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.setTemplate(factory.getReportTemplate());

        report.title(getUnitTitleComponent());

        report.detail(getUnitDetailComponent());

        report.detailFooter(Components.verticalGap(20));

        return report;
    }

    private final SubreportBuilder createWeaponsSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.detail(factory
                .getBorderedCellComponent(getWeaponDetailComponent()));

        return Components.subreport(report);
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final ComponentBuilder<?, ?> getArmorComponent() {
        final ComponentBuilder<?, ?> armorName;
        final ComponentBuilder<?, ?> armorArmor;

        armorName = factory
                .getBorderedCellComponent(Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                "armor.name")), Components.text(factory
                        .getArmorNameField("armor", getLocalizationService()))));
        armorArmor = factory.getBorderedCellComponent(Components
                .horizontalList(Components.text(getLocalizationService()
                        .getReportString("armor.armor")), Components
                        .text(factory.getArmorArmorField("armor"))));

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

        actions = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "actions.short")),
                Components.text(factory.getIntegerField("actions"))));
        combat = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "combat.short")),
                Components.text(factory.getIntegerField("combat"))));
        precision = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "precision.short")),
                Components.text(factory.getIntegerField("precision"))));
        agility = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "agility.short")),
                Components.text(factory.getIntegerField("agility"))));
        strength = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "strength.short")),
                Components.text(factory.getIntegerField("strength"))));
        toughness = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "toughness.short")),
                Components.text(factory.getIntegerField("toughness"))));
        tech = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "tech.short")),
                Components.text(factory.getIntegerField("tech"))));

        return Components.horizontalList(actions, combat, precision, agility,
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
                getLocalizationService().getReportString("rules"),
                getLocalizationService()).setDataSource(
                Expressions.subDatasourceBeanCollection("specialRules")));

        attributes = getAttributesComponent();

        armor = getArmorComponent();

        equipment = factory.getBorderedCellComponent(factory
                .getEquipmentSubreport(
                        getLocalizationService().getReportString("equipment"),
                        getLocalizationService()).setDataSource(
                        Expressions.subDatasourceBeanCollection("equipment")));

        weapons = factory.getBorderedCellComponent(createWeaponsSubreport()
                .setDataSource(
                        Expressions.subDatasourceBeanCollection("weapons")));

        return Components.verticalList(attributes, rules, armor, equipment,
                weapons);
    }

    private final ComponentBuilder<?, ?> getUnitTitleComponent() {
        ComponentBuilder<?, ?> name;
        ComponentBuilder<?, ?> points;

        name = factory.getBorderedCellComponent(Components.text(factory
                .getUnitNameField("_THIS", getLocalizationService())));
        points = factory.getBorderedCellComponent(Components.horizontalList(
                Components.text(getLocalizationService().getReportString(
                        "valoration")), Components.text(factory
                        .getValueHandlerValueField("valoration"))));

        return Components.horizontalList(name, points);
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent() {
        final ComponentBuilder<?, ?> data;
        final SubreportBuilder rules;
        final SubreportBuilder equipment;

        rules = factory.getRulesSubreport(getLocalizationService()
                .getReportString("rules"), getLocalizationService());
        rules.setDataSource(Expressions
                .subDatasourceBeanCollection("specialRules"));

        equipment = factory.getWeaponEnhancementsSubreport(
                getLocalizationService().getReportString("enhancements"),
                getLocalizationService());
        equipment.setDataSource(Expressions
                .subDatasourceBeanCollection("enhancements"));

        data = Components.verticalList(Components.text(factory
                .getWeaponNameField("_THIS", getLocalizationService())),
                Components.horizontalList(Components
                        .text(getLocalizationService()
                                .getReportString("combat")), Components
                        .text(factory.getWeaponCombatField("_THIS"))),
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                "strength")), Components.text(factory
                        .getWeaponStrengthField("_THIS"))), Components
                        .horizontalList(Components
                                .text(getLocalizationService().getReportString(
                                        "penetration")), Components
                                .text(factory
                                        .getWeaponPenetrationField("_THIS"))),
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                "distance.metric")), Components.text(factory
                        .getWeaponDistanceMetricField("_THIS"))),
                Components.horizontalList(Components
                        .text(getLocalizationService().getReportString(
                                "distance.imperial")), Components.text(factory
                        .getWeaponDistanceImperialField("_THIS"))), factory
                        .getBorderedCellComponent(equipment), factory
                        .getBorderedCellComponent(rules));

        return data;
    }

}
