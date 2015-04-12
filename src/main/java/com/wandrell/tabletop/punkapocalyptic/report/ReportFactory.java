package com.wandrell.tabletop.punkapocalyptic.report;

import java.io.InputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.expression.Expressions;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.WhenNoDataType;

import com.wandrell.tabletop.punkapocalyptic.conf.ReportBundleConf;
import com.wandrell.tabletop.punkapocalyptic.conf.ReportConf;
import com.wandrell.tabletop.punkapocalyptic.conf.factory.DynamicReportsFactory;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Armor;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.ArmorDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.EquipmentDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.FactionDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.GangDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.MutationDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.SpecialRulesDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.UnitDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.WeaponDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.WeaponEnhancementDataType;
import com.wandrell.tabletop.punkapocalyptic.report.expression.CurrentObjectDatasourceExpression;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.ArmorArmorFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.ArmorNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.FactionNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangBulletsFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangUnitsRangeFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangValorationFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GroupedUnitNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.UnitValorationFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponCombatFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponDistanceImperialFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponDistanceMetricFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponPenetrationFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.WeaponStrengthFormatter;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.util.ResourceUtils;

public final class ReportFactory {

    private static final ReportFactory instance = new ReportFactory();

    public static final ReportFactory getInstance() {
        return instance;
    }

    private ReportFactory() {
        super();
    }

    public final JasperReportBuilder getGangReport(final String appName,
            final String version, final String URL, final String imagePath,
            final LocalizationService localizationService,
            final RulesetService rulesetService) {
        return buildReport(appName, version, URL, imagePath,
                localizationService, rulesetService);
    }

    private final JasperReportBuilder buildReport(final String appName,
            final String version, final String URL, final String imagePath,
            final LocalizationService localizationService,
            final RulesetService rulesetService) {
        final JasperReportBuilder report;
        final ComponentBuilder<?, ?> title;
        final ComponentBuilder<?, ?> footer;
        final SubreportBuilder subreport;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        report = DynamicReports.report();
        report.setTemplate(factory.getReportTemplate());

        title = getGangReportTitle(appName, version, URL, imagePath,
                localizationService, rulesetService);
        subreport = getUnitsSubreport(localizationService);
        footer = factory.getReportFooter();

        report.title(title);
        report.detailFooter(subreport);
        report.pageFooter(footer);

        return report;
    }

    private final SubreportBuilder createWeaponsSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;

        report = DynamicReports.report();
        report.detail(DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(
                        getWeaponDetailComponent(localizationService)));

        return Components.subreport(report);
    }

    private final DRField<Armor> getArmorArmorField(final String fieldName,
            final LocalizationService localizationService) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorArmorFormatter(
                localizationService)));

        return field;
    }

    private final DRField<Armor> getArmorNameField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorNameFormatter()));

        return field;
    }

    private final SubreportBuilder getEquipmentSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;
        final DRField<Equipment> field;

        field = new DRField<Equipment>(ReportConf.CURRENT, Equipment.class);
        field.setDataType(new EquipmentDataType());

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(localizationService
                .getReportString(ReportBundleConf.EQUIPMENT)));

        return Components.subreport(report);
    }

    private final DRField<Faction> getFactionNameField(final String fieldName) {
        final DRField<Faction> field;

        field = new DRField<Faction>(fieldName, Faction.class);
        field.setDataType(new FactionDataType(new FactionNameFormatter()));

        return field;
    }

    private final DRField<Gang> getGangBulletsField(final String fieldName,
            final LocalizationService localizationService) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangBulletsFormatter(
                localizationService)));

        return field;
    }

    private final ComponentBuilder<?, ?> getGangReportTitle(
            final String appName, final String version, final String URL,
            final String imagePath,
            final LocalizationService localizationService,
            final RulesetService rulesetService) {
        final ComponentBuilder<?, ?> brand;
        final VerticalListBuilder gangData;
        final DynamicReportsFactory factory;
        final InputStream imageStream;

        imageStream = ResourceUtils.getClassPathInputStream(imagePath);

        factory = DynamicReportsFactory.getInstance();

        brand = factory.getTitleLabelComponent(imageStream, appName, version,
                URL);
        brand.setStyle(Styles.style().setRightBorder(Styles.pen1Point()));

        gangData = Components.verticalList();
        // Faction
        gangData.add(Components.text(getFactionNameField(ReportConf.FACTION))
                .setStyle(factory.getTitleStyle()));
        // Valoration
        gangData.add(Components.text(getGangValorationField(ReportConf.CURRENT,
                localizationService)));
        // Units
        gangData.add(Components.text(getGangUnitsField(ReportConf.CURRENT,
                localizationService, rulesetService)));

        // Bullets
        gangData.add(Components.text(getGangBulletsField(ReportConf.CURRENT,
                localizationService)));

        return Components.verticalList().add(
                Components.horizontalList().add(brand,
                        Components.horizontalGap(5), gangData),
                Components.line(), Components.verticalGap(10));
    }

    private final DRField<Gang> getGangUnitsField(final String fieldName,
            final LocalizationService localizationService,
            final RulesetService rulesetService) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangUnitsRangeFormatter(
                localizationService, rulesetService)));

        return field;
    }

    private final DRField<Gang> getGangValorationField(final String fieldName,
            final LocalizationService localizationService) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangValorationFormatter(
                localizationService)));

        return field;
    }

    private final SubreportBuilder getMutationSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;
        final DRField<Mutation> field;

        field = new DRField<Mutation>(ReportConf.CURRENT, Mutation.class);
        field.setDataType(new MutationDataType());

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(localizationService
                .getReportString(ReportBundleConf.MUTATIONS)));

        return Components.subreport(report);
    }

    private final SubreportBuilder getRulesSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;
        final DRField<SpecialRule> field;

        field = new DRField<SpecialRule>(ReportConf.CURRENT, SpecialRule.class);
        field.setDataType(new SpecialRulesDataType());

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(localizationService
                .getReportString(ReportBundleConf.RULES)));
        report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

        return Components.subreport(report);
    }

    private final SubreportBuilder getRulesSubreportForUnit(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;
        final DRField<SpecialRule> field;

        field = new DRField<SpecialRule>(ReportConf.CURRENT, SpecialRule.class);
        field.setDataType(new SpecialRulesDataType());

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(localizationService
                .getReportString(ReportBundleConf.RULES)));

        return Components.subreport(report);
    }

    private final ComponentBuilder<?, ?> getUnitArmorSubreport(
            final LocalizationService localizationService) {
        final TextFieldBuilder<String> armorNameLabelText;
        final TextFieldBuilder<Armor> armorNameText;
        final TextFieldBuilder<Armor> armorArmorText;

        armorNameLabelText = Components.text(localizationService
                .getReportString(ReportBundleConf.ARMOR_NAME));
        armorNameText = Components.text(getArmorNameField(ReportConf.ARMOR));

        armorArmorText = Components.text(getArmorArmorField(ReportConf.ARMOR,
                localizationService));

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

    private final ComponentBuilder<?, ?> getUnitAttributesSubreport(
            final LocalizationService localizationService) {
        final HorizontalListBuilder list;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        list = Components.horizontalList();
        list.add(factory.getBorderedCellComponentThin(Components.text(
                localizationService.getReportString(ReportBundleConf.ACTIONS))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                localizationService.getReportString(ReportBundleConf.COMBAT))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components
                .text(localizationService
                        .getReportString(ReportBundleConf.PRECISION))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                localizationService.getReportString(ReportBundleConf.AGILITY))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                localizationService.getReportString(ReportBundleConf.STRENGTH))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components
                .text(localizationService
                        .getReportString(ReportBundleConf.TOUGHNESS))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));
        list.add(factory.getBorderedCellComponentThin(Components.text(
                localizationService.getReportString(ReportBundleConf.TECH))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)));

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

    private final ComponentBuilder<?, ?> getUnitDetailComponent(
            final LocalizationService localizationService) {
        final ComponentBuilder<?, ?> rules;
        final ComponentBuilder<?, ?> attributes;
        final ComponentBuilder<?, ?> armor;
        final ComponentBuilder<?, ?> equipment;
        final ComponentBuilder<?, ?> weapons;
        final ComponentBuilder<?, ?> mutations;

        attributes = getUnitAttributesSubreport(localizationService);
        armor = getUnitArmorSubreport(localizationService);
        weapons = getUnitWeaponSubreport(localizationService);
        equipment = getUnitEquipmentSubreport(localizationService);
        rules = getUnitRulesSubreport(localizationService);
        mutations = getUnitMutationSubreport(localizationService);

        return Components.verticalList(attributes, armor, weapons, equipment,
                rules, mutations);
    }

    private final ComponentBuilder<?, ?> getUnitEquipmentSubreport(
            final LocalizationService localizationService) {
        final SubreportBuilder subreport;

        subreport = getEquipmentSubreport(localizationService);
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.EQUIPMENT));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(subreport);
    }

    private final ComponentBuilder<?, ?> getUnitMutationSubreport(
            final LocalizationService localizationService) {
        final SubreportBuilder subreport;

        subreport = getMutationSubreport(localizationService);
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.MUTATIONS));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(subreport);
    }

    private final DRField<Unit> getUnitNameField(final String fieldName) {
        final DRField<Unit> field;

        field = new DRField<Unit>(fieldName, Unit.class);
        field.setDataType(new UnitDataType(new GroupedUnitNameFormatter()));

        return field;
    }

    private final ComponentBuilder<?, ?> getUnitReportTitle(
            final LocalizationService localizationService) {
        final ComponentBuilder<?, ?> unitName;
        final ComponentBuilder<?, ?> unitValoration;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        // Unit valoration fields
        unitValoration = Components.text(getUnitValorationField(
                ReportConf.CURRENT, localizationService));

        // Unit name
        unitName = Components.text(getUnitNameField(ReportConf.CURRENT));

        return factory.getBorderedCellComponentThin(Components.verticalList(
                unitName, unitValoration));
    }

    private final ComponentBuilder<?, ?> getUnitRulesSubreport(
            final LocalizationService localizationService) {
        final SubreportBuilder subreport;

        subreport = getRulesSubreportForUnit(localizationService);
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(subreport);
    }

    private final SubreportBuilder getUnitsSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder unitsReport;
        final JasperReportBuilder unitReport;
        final SubreportBuilder subreport;
        final ComponentBuilder<?, ?> unitTitle;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        unitReport = DynamicReports.report();

        unitTitle = getUnitReportTitle(localizationService);

        unitReport.setTemplate(factory.getReportTemplate());
        unitReport.title(unitTitle);
        unitReport.detail(getUnitDetailComponent(localizationService));
        unitReport.detailFooter(Components.verticalGap(20));

        unitReport.setTitleStyle(Styles.style()
                .setTopBorder(Styles.pen2Point()));
        unitReport.setDetailStyle(Styles.style().setBottomBorder(
                Styles.pen2Point()));

        unitsReport = DynamicReports.report();
        unitsReport.setTemplate(factory.getReportTemplate());

        subreport = Components.subreport(unitReport).setDataSource(
                new CurrentObjectDatasourceExpression(ReportConf.CURRENT));

        unitsReport.addDetail(subreport);

        return Components.subreport(unitsReport).setDataSource(
                Expressions.subDatasourceBeanCollection(ReportConf.UNITS));
    }

    private final DRField<Unit> getUnitValorationField(final String fieldName,
            final LocalizationService service) {
        final DRField<Unit> field;

        field = new DRField<Unit>(fieldName, Unit.class);
        field.setDataType(new UnitDataType(new UnitValorationFormatter(service)));

        return field;
    }

    private final ComponentBuilder<?, ?> getUnitWeaponSubreport(
            final LocalizationService localizationService) {
        final SubreportBuilder subreport;

        subreport = createWeaponsSubreport(localizationService);
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPONS));

        return DynamicReportsFactory.getInstance()
                .getBorderedCellComponentThin(subreport);
    }

    private final DRField<Weapon> getWeaponCombatField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponCombatFormatter()));

        return field;
    }

    private final ComponentBuilder<?, ?> getWeaponDetailComponent(
            final LocalizationService localizationService) {
        final ComponentBuilder<?, ?> data;
        final ComponentBuilder<?, ?> attributes;
        final SubreportBuilder rules;
        final SubreportBuilder equipment;

        rules = getRulesSubreport(localizationService);
        rules.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.SPECIAL_RULES));
        rules.setHeight(10);

        equipment = getWeaponEnhancementsSubreport(localizationService);
        equipment.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.WEAPON_ENHANCEMENTS));
        equipment.setHeight(10);

        attributes = Components
                .verticalList(
                        Components.text(getWeaponNameField(ReportConf.CURRENT,
                                localizationService)),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(localizationService
                                        .getReportString(ReportBundleConf.COMBAT)),
                                Components
                                        .text(getWeaponCombatField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(localizationService
                                        .getReportString(ReportBundleConf.STRENGTH)),
                                Components
                                        .text(getWeaponStrengthField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(localizationService
                                        .getReportString(ReportBundleConf.PENETRATION)),
                                Components
                                        .text(getWeaponPenetrationField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(localizationService
                                        .getReportString(ReportBundleConf.DISTANCE_METRIC)),
                                Components
                                        .text(getWeaponDistanceMetricField(ReportConf.CURRENT))),
                        Components.horizontalList(
                                Components.horizontalGap(10),
                                Components.text(localizationService
                                        .getReportString(ReportBundleConf.DISTANCE_IMPERIAL)),
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

    private final SubreportBuilder getWeaponEnhancementsSubreport(
            final LocalizationService localizationService) {
        final JasperReportBuilder report;
        final DRField<WeaponEnhancement> field;

        field = new DRField<WeaponEnhancement>(ReportConf.CURRENT,
                WeaponEnhancement.class);
        field.setDataType(new WeaponEnhancementDataType());

        report = DynamicReports.report();
        report.detail(Components.horizontalList(Components.horizontalGap(10),
                Components.verticalList(Components.text(field))));
        report.title(Components.text(localizationService
                .getReportString(ReportBundleConf.ENHANCEMENTS)));
        report.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

        return Components.subreport(report);
    }

    private final DRField<Weapon> getWeaponNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponNameFormatter()));

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
