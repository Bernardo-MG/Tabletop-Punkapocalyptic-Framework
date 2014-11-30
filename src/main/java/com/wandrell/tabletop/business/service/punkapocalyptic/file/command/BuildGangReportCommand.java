package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.expression.AbstractSubDatasourceExpression;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.punkapocalyptic.ArmorUtils;
import com.wandrell.tabletop.business.util.punkapocalyptic.WeaponUtils;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;

public final class BuildGangReportCommand implements
        ReturnCommand<JasperReportBuilder>, ApplicationInfoServiceAware,
        LocalizationServiceAware {

    private ApplicationInfoService       appInfoService;
    private final StyleBuilder           bold22CenteredStyle;
    private final StyleBuilder           boldCenteredStyle;
    private final StyleBuilder           boldStyle;
    private final StyleBuilder           columnStyle;
    private final StyleBuilder           columnTitleStyle;
    private ComponentBuilder<?, ?>       dynamicReportsComponent;
    private final ComponentBuilder<?, ?> footerComponent;
    private final Gang                   gang;
    private final StyleBuilder           groupStyle;
    private final StyleBuilder           italicStyle;
    private LocalizationService          localizationService;
    private final String                 path;
    private final ReportTemplateBuilder  reportTemplate;
    private final StyleBuilder           rootStyle;
    private final StyleBuilder           subtotalStyle;

    private class ArmorArmorFormatter extends
            AbstractValueFormatter<String, Armor> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Armor value, ReportParameters reportParameters) {
            return ArmorUtils.getArmor(value);
        }
    }

    private class ArmorDataType extends AbstractDataType<Armor, Armor> {

        private static final long                      serialVersionUID = 1L;
        private final DRIValueFormatter<String, Armor> formatter;

        public ArmorDataType(final DRIValueFormatter<String, Armor> formatter) {
            super();

            this.formatter = formatter;
        }

        @Override
        public String getPattern() {
            return Defaults.getDefaults().getStringType().getPattern();
        }

        @Override
        public DRIValueFormatter<String, Armor> getValueFormatter() {
            return formatter;
        }

        @Override
        public String valueToString(Armor value, Locale locale) {
            return value.getName();
        }

    }

    private class ArmorNameFormatter extends
            AbstractValueFormatter<String, Armor> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Armor value, ReportParameters reportParameters) {
            return value.getName();
        }
    }

    private class CurrentSubDatasourceExpression extends
            AbstractSubDatasourceExpression<Unit> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        public CurrentSubDatasourceExpression(
                DRIExpression<? extends Unit> expression) {
            super(expression);
        }

        public CurrentSubDatasourceExpression(String fieldName) {
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

    private class ValueHandlerDataType extends
            AbstractDataType<ValueHandler, ValueHandler> {

        private static final long                             serialVersionUID = 1L;
        private final DRIValueFormatter<String, ValueHandler> formatter        = new ValueHandlerFormatter();

        private class ValueHandlerFormatter extends
                AbstractValueFormatter<String, ValueHandler> {
            private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

            @Override
            public String format(ValueHandler value,
                    ReportParameters reportParameters) {
                return String.valueOf(value.getValue());
            }
        }

        public ValueHandlerDataType() {
            super();
        }

        @Override
        public String getPattern() {
            return Defaults.getDefaults().getStringType().getPattern();
        }

        @Override
        public DRIValueFormatter<String, ValueHandler> getValueFormatter() {
            return formatter;
        }

        @Override
        public String valueToString(ValueHandler value, Locale locale) {
            return String.valueOf(value.getValue());
        }

    }

    private class WeaponCombatFormatter extends
            AbstractValueFormatter<String, Weapon> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Weapon value, ReportParameters reportParameters) {
            final String result;

            if (value instanceof MeleeWeapon) {
                result = String.valueOf(((MeleeWeapon) value).getCombat());
            } else {
                result = "-";
            }

            return result;
        }
    }

    private class WeaponDataType extends AbstractDataType<Weapon, Weapon> {

        private static final long                       serialVersionUID = 1L;
        private final DRIValueFormatter<String, Weapon> formatter;

        public WeaponDataType(final DRIValueFormatter<String, Weapon> formatter) {
            super();

            this.formatter = formatter;
        }

        @Override
        public String getPattern() {
            return Defaults.getDefaults().getStringType().getPattern();
        }

        @Override
        public DRIValueFormatter<String, Weapon> getValueFormatter() {
            return formatter;
        }

        @Override
        public String valueToString(Weapon value, Locale locale) {
            return String.valueOf(value.getName());
        }

    }

    private class WeaponDistanceImperialFormatter extends
            AbstractValueFormatter<String, Weapon> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Weapon value, ReportParameters reportParameters) {
            final String result;

            if (value instanceof MeleeWeapon) {
                result = "-";
            } else {
                result = WeaponUtils
                        .getRangedWeaponDistanceImperial((RangedWeapon) value);
            }

            return result;
        }
    }

    private class WeaponDistanceMetricFormatter extends
            AbstractValueFormatter<String, Weapon> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Weapon value, ReportParameters reportParameters) {
            final String result;

            if (value instanceof MeleeWeapon) {
                result = "-";
            } else {
                result = WeaponUtils
                        .getRangedWeaponDistanceMetric((RangedWeapon) value);
            }

            return result;
        }
    }

    private class WeaponPenetrationFormatter extends
            AbstractValueFormatter<String, Weapon> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Weapon value, ReportParameters reportParameters) {
            final String result;

            if (value instanceof MeleeWeapon) {
                result = String.valueOf(((MeleeWeapon) value).getPenetration());
            } else {
                result = WeaponUtils
                        .getRangedWeaponPenetration((RangedWeapon) value);
            }

            return result;
        }
    }

    private class WeaponStrengthFormatter extends
            AbstractValueFormatter<String, Weapon> {
        private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

        @Override
        public String format(Weapon value, ReportParameters reportParameters) {
            final String result;

            if (value instanceof MeleeWeapon) {
                result = String.valueOf(((MeleeWeapon) value).getStrength());
            } else {
                result = WeaponUtils
                        .getRangedWeaponStrength((RangedWeapon) value);
            }

            return result;
        }
    }

    {
        rootStyle = stl.style().setPadding(2);
        boldStyle = stl.style(rootStyle).bold();
        italicStyle = stl.style(rootStyle).italic();

        boldCenteredStyle = stl.style(boldStyle).setAlignment(
                HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);

        columnStyle = stl.style(rootStyle).setVerticalAlignment(
                VerticalAlignment.MIDDLE);
        columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setBackgroundColor(Color.LIGHT_GRAY).bold();

        groupStyle = stl.style(boldStyle).setHorizontalAlignment(
                HorizontalAlignment.LEFT);

        subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

        StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
        StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(170, 170, 170));
        StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(140, 140, 140));
        StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(
                stl.pen1Point());

        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
                .setHeadingStyle(0, stl.style(rootStyle).bold());

        footerComponent = cmp.pageXofY().setStyle(
                stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));

        reportTemplate = template().setLocale(Locale.ENGLISH)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle).highlightDetailEvenRows()
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(crosstabGroupStyle)
                .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                .setCrosstabCellStyle(crosstabCellStyle)
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);
    }

    public BuildGangReportCommand(final String path, final Gang gang) {
        super();

        this.gang = gang;
        this.path = path;
    }

    @Override
    public final JasperReportBuilder execute() throws DRException {
        JasperReportBuilder report;
        SubreportBuilder subreport;

        report = DynamicReports.report();
        subreport = createUnitListSubreport(DynamicReports.exp
                .subDatasourceBeanCollection("units"));

        HyperLinkBuilder link = hyperLink(getApplicationInfoService()
                .getDownloadURI().toString());
        dynamicReportsComponent = cmp.horizontalList(
                cmp.image(ResourceUtils.getClassPathURL(getImagePath()))
                        .setFixedDimension(60, 60),
                cmp.verticalList(
                        cmp.text(
                                getApplicationInfoService()
                                        .getApplicationName())
                                .setStyle(bold22CenteredStyle)
                                .setHorizontalAlignment(
                                        HorizontalAlignment.LEFT),
                        cmp.text(
                                getApplicationInfoService().getDownloadURI()
                                        .toString()).setStyle(italicStyle)
                                .setHyperLink(link))).setFixedWidth(300);

        report.setTemplate(reportTemplate);
        report.title(createTitleComponent());
        report.pageFooter(footerComponent);
        report.detailFooter(subreport);

        final Collection<Gang> gangs;
        gangs = new LinkedList<>();
        gangs.add(gang);
        report.setDataSource(gangs);

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

    private ComponentBuilder<?, ?> createCellComponent(
            ComponentBuilder<?, ?> content) {
        VerticalListBuilder cell = cmp.verticalList(cmp.horizontalList(
                cmp.horizontalGap(20), content, cmp.horizontalGap(5)));
        cell.setStyle(stl.style(stl.pen2Point()));
        return cell;
    }

    private SubreportBuilder createEquipmentSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.columns(DynamicReports.col.column("equipment", "name",
                DynamicReports.type.stringType()));

        return cmp.subreport(report);
    }

    private SubreportBuilder createRulesSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.columns(DynamicReports.col.column("rules", "name",
                DynamicReports.type.stringType()));

        return cmp.subreport(report);
    }

    private final ComponentBuilder<?, ?> createTitleComponent() {
        return cmp
                .horizontalList()
                .add(dynamicReportsComponent)
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.text(getLocalizationService().getViewString("faction")))
                .add(cmp.text(getLocalizationService().getFactionNameString(
                        getGang().getFaction().getName())))
                .newRow()
                .add(cmp.text(getLocalizationService().getViewString(
                        "valoration")))
                .add(cmp.text(getGang().getValoration().getValue()))
                .newRow()
                .add(cmp.text(getLocalizationService().getViewString("bullets")))
                .add(cmp.text(getGang().getBullets().getValue())).newRow()
                .add(cmp.text(getLocalizationService().getViewString("units")))
                .add(cmp.text(getGang().getUnits().size())).newRow()
                .add(cmp.line()).newRow().add(cmp.verticalGap(10));
    }

    private SubreportBuilder createUnitListSubreport(
            DRIExpression<JRDataSource> dataSource) {
        SubreportBuilder subreport;

        subreport = cmp.subreport(createUnitSubreport()).setDataSource(
                dataSource);

        JasperReportBuilder report = DynamicReports.report();
        report.setTemplate(reportTemplate);
        report.addDetail(subreport);
        subreport.setDataSource(new CurrentSubDatasourceExpression("_THIS"));

        return cmp.subreport(report).setDataSource(dataSource);
    }

    private JasperReportBuilder createUnitSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.setTemplate(reportTemplate);

        report.title(getUnitTitleComponent());

        report.detail(getUnitDetailComponent());

        report.detailFooter(cmp.verticalGap(20));

        // report.columns(DynamicReports.col.column("actions", "actions",
        // DynamicReports.type.integerType()), DynamicReports.col.column(
        // "combat", "combat", DynamicReports.type.integerType()),
        // DynamicReports.col.column("precision", "precision",
        // DynamicReports.type.integerType()), DynamicReports.col
        // .column("agility", "agility",
        // DynamicReports.type.integerType()),
        // DynamicReports.col.column("strength", "strength",
        // DynamicReports.type.integerType()), DynamicReports.col
        // .column("toughness", "toughness",
        // DynamicReports.type.integerType()),
        // DynamicReports.col.column("tech", "tech",
        // DynamicReports.type.integerType()));

        return report;
    }

    private SubreportBuilder createWeaponsSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.detail(createCellComponent(getWeaponDetailComponent()));

        return cmp.subreport(report);
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final DRField<Armor> getArmorArmorField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorArmorFormatter()));

        return field;
    }

    private final DRField<Armor> getArmorNameField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorNameFormatter()));

        return field;
    }

    private final Gang getGang() {
        return gang;
    }

    private final String getImagePath() {
        return path;
    }

    private final DRField<Integer> getIntegerField(final String fieldName) {
        final DRField<Integer> field;

        field = new DRField<Integer>(fieldName, Integer.class);
        field.setDataType(DynamicReports.type.integerType());

        return field;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final DRField<String> getStringField(final String fieldName) {
        final DRField<String> field;

        field = new DRField<String>(fieldName, String.class);
        field.setDataType(DynamicReports.type.stringType());

        return field;
    }

    private final ComponentBuilder<?, ?> getUnitDetailComponent() {
        final ComponentBuilder<?, ?> rules;
        final ComponentBuilder<?, ?> attributes;
        final ComponentBuilder<?, ?> armor;
        final ComponentBuilder<?, ?> equipment;
        final ComponentBuilder<?, ?> weapons;
        final ComponentBuilder<?, ?> armorName;
        final ComponentBuilder<?, ?> armorArmor;
        final ComponentBuilder<?, ?> actions;
        final ComponentBuilder<?, ?> combat;
        final ComponentBuilder<?, ?> precision;
        final ComponentBuilder<?, ?> agility;
        final ComponentBuilder<?, ?> strength;
        final ComponentBuilder<?, ?> toughness;
        final ComponentBuilder<?, ?> tech;

        actions = createCellComponent(cmp.horizontalList(cmp.text("a"),
                cmp.text(getIntegerField("actions"))));
        combat = createCellComponent(cmp.horizontalList(cmp.text("c"),
                cmp.text(getIntegerField("combat"))));
        precision = createCellComponent(cmp.horizontalList(cmp.text("p"),
                cmp.text(getIntegerField("precision"))));
        agility = createCellComponent(cmp.horizontalList(cmp.text("ag"),
                cmp.text(getIntegerField("agility"))));
        strength = createCellComponent(cmp.horizontalList(cmp.text("f"),
                cmp.text(getIntegerField("strength"))));
        toughness = createCellComponent(cmp.horizontalList(cmp.text("d"),
                cmp.text(getIntegerField("toughness"))));
        tech = createCellComponent(cmp.horizontalList(cmp.text("t"),
                cmp.text(getIntegerField("tech"))));

        armorName = createCellComponent(cmp.horizontalList(cmp.text("armor"),
                cmp.text(getArmorNameField("armor"))));
        armorArmor = createCellComponent(cmp.horizontalList(cmp.text("armor"),
                cmp.text(getArmorArmorField("armor"))));

        rules = createCellComponent(createRulesSubreport().setDataSource(
                DynamicReports.exp.subDatasourceBeanCollection("specialRules")));

        attributes = cmp.horizontalList(actions, combat, precision, agility,
                strength, toughness, tech);

        armor = cmp.horizontalList(armorName, armorArmor);

        equipment = createCellComponent(createEquipmentSubreport()
                .setDataSource(
                        DynamicReports.exp
                                .subDatasourceBeanCollection("equipment")));

        weapons = createCellComponent(createWeaponsSubreport().setDataSource(
                DynamicReports.exp.subDatasourceBeanCollection("weapons")));

        return cmp.verticalList(attributes, rules, armor, equipment, weapons);
    }

    private final ComponentBuilder<?, ?> getUnitTitleComponent() {
        ComponentBuilder<?, ?> name;
        ComponentBuilder<?, ?> points;

        name = createCellComponent(cmp.text(new DRField<String>("unitName",
                String.class)));
        points = createCellComponent(cmp.horizontalList(cmp.text("points"),
                cmp.text(getValueHandlerField("valoration"))));

        return cmp.horizontalList(name, points);
    }

    private final DRField<ValueHandler> getValueHandlerField(
            final String fieldName) {
        final DRField<ValueHandler> field;

        field = new DRField<ValueHandler>(fieldName, ValueHandler.class);
        field.setDataType(new ValueHandlerDataType());

        return field;
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

        rules = createRulesSubreport();
        rules.setDataSource(DynamicReports.exp
                .subDatasourceBeanCollection("specialRules"));

        data = cmp.verticalList(
                cmp.text(getStringField("name")),
                cmp.horizontalList(cmp.text("combat"),
                        cmp.text(getWeaponCombatField("_THIS"))),
                cmp.horizontalList(cmp.text("strength"),
                        cmp.text(getWeaponStrengthField("_THIS"))),
                cmp.horizontalList(cmp.text("penetration"),
                        cmp.text(getWeaponPenetrationField("_THIS"))),
                cmp.horizontalList(cmp.text("distancem"),
                        cmp.text(getWeaponDistanceMetricField("_THIS"))),
                cmp.horizontalList(cmp.text("distancei"),
                        cmp.text(getWeaponDistanceImperialField("_THIS"))),
                createCellComponent(rules));

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
