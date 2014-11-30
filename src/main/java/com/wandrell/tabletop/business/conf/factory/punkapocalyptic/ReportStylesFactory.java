package com.wandrell.tabletop.business.conf.factory.punkapocalyptic;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.report.punkapocalyptic.datatype.ArmorDataType;
import com.wandrell.tabletop.business.report.punkapocalyptic.datatype.CollectionDataType;
import com.wandrell.tabletop.business.report.punkapocalyptic.datatype.FactionDataType;
import com.wandrell.tabletop.business.report.punkapocalyptic.datatype.ValueHandlerDataType;
import com.wandrell.tabletop.business.report.punkapocalyptic.datatype.WeaponDataType;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.ArmorArmorFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.ArmorNameFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.CollectionSizeFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.WeaponCombatFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.WeaponDistanceImperialFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.WeaponDistanceMetricFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.WeaponPenetrationFormatter;
import com.wandrell.tabletop.business.report.punkapocalyptic.formatter.WeaponStrengthFormatter;

public final class ReportStylesFactory {

    private static final StyleBuilder           bold22CenteredStyle;
    private static final StyleBuilder           boldCenteredStyle;
    private static final StyleBuilder           boldStyle;
    private static final StyleBuilder           columnStyle;
    private static final StyleBuilder           columnTitleStyle;
    private static final ComponentBuilder<?, ?> footerComponent;
    private static final StyleBuilder           groupStyle;
    private static final ReportStylesFactory    instance = new ReportStylesFactory();
    private static final StyleBuilder           italicStyle;
    private static final ReportTemplateBuilder  reportTemplate;
    private static final StyleBuilder           rootStyle;
    private static final StyleBuilder           subtotalStyle;

    static {
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

    public static final ReportStylesFactory getInstance() {
        return instance;
    }

    private ReportStylesFactory() {
        super();
    }

    public final DRField<Armor> getArmorArmorField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorArmorFormatter()));

        return field;
    }

    public final DRField<Armor> getArmorNameField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorNameFormatter()));

        return field;
    }

    public final ComponentBuilder<?, ?> getBorderedCellComponent(
            ComponentBuilder<?, ?> content) {
        VerticalListBuilder cell = cmp.verticalList(cmp.horizontalList(
                cmp.horizontalGap(20), content, cmp.horizontalGap(5)));
        cell.setStyle(stl.style(stl.pen2Point()));
        return cell;
    }

    public final DRField<Collection<?>> getCollectionSizeField(
            final String fieldName) {
        final DRField<Collection<?>> field;

        field = new DRField<Collection<?>>(fieldName, Collection.class);
        field.setDataType(new CollectionDataType(new CollectionSizeFormatter()));

        return field;
    }

    public final SubreportBuilder getEquipmentSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.columns(DynamicReports.col.column("equipment", "name",
                DynamicReports.type.stringType()));

        return cmp.subreport(report);
    }

    public final DRField<Faction> getFactionField(final String fieldName) {
        final DRField<Faction> field;

        field = new DRField<Faction>(fieldName, Faction.class);
        field.setDataType(new FactionDataType());

        return field;
    }

    public final DRField<Integer> getIntegerField(final String fieldName) {
        final DRField<Integer> field;

        field = new DRField<Integer>(fieldName, Integer.class);
        field.setDataType(DynamicReports.type.integerType());

        return field;
    }

    public final ComponentBuilder<?, ?> getReportFooter() {
        return footerComponent;
    }

    public final ReportTemplateBuilder getReportTemplate() {
        return reportTemplate;
    }

    public final SubreportBuilder getRulesSubreport() {
        JasperReportBuilder report = DynamicReports.report();

        report.columns(DynamicReports.col.column("rules", "name",
                DynamicReports.type.stringType()));

        return cmp.subreport(report);
    }

    public final DRField<String> getStringField(final String fieldName) {
        final DRField<String> field;

        field = new DRField<String>(fieldName, String.class);
        field.setDataType(DynamicReports.type.stringType());

        return field;
    }

    public final ComponentBuilder<?, ?> getTitleLabelComponent(
            final InputStream image, final String appName,
            final String downloadURL) {
        final ComponentBuilder<?, ?> titleLabelComponent;
        final HyperLinkBuilder link;

        link = hyperLink(downloadURL);

        titleLabelComponent = cmp.horizontalList(
                cmp.image(image).setFixedDimension(60, 60),
                cmp.verticalList(
                        cmp.text(appName)
                                .setStyle(bold22CenteredStyle)
                                .setHorizontalAlignment(
                                        HorizontalAlignment.LEFT),
                        cmp.text(downloadURL).setStyle(italicStyle)
                                .setHyperLink(link))).setFixedWidth(300);

        return titleLabelComponent;
    }

    public final DRField<ValueHandler> getValueHandlerValueField(
            final String fieldName) {
        final DRField<ValueHandler> field;

        field = new DRField<ValueHandler>(fieldName, ValueHandler.class);
        field.setDataType(new ValueHandlerDataType());

        return field;
    }

    public final DRField<Weapon> getWeaponCombatField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponCombatFormatter()));

        return field;
    }

    public final DRField<Weapon> getWeaponDistanceImperialField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(
                new WeaponDistanceImperialFormatter()));

        return field;
    }

    public final DRField<Weapon> getWeaponDistanceMetricField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(
                new WeaponDistanceMetricFormatter()));

        return field;
    }

    public final DRField<Weapon> getWeaponPenetrationField(
            final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponPenetrationFormatter()));

        return field;
    }

    public final DRField<Weapon> getWeaponStrengthField(final String fieldName) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponStrengthFormatter()));

        return field;
    }

}
