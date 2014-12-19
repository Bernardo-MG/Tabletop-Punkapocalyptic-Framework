package com.wandrell.tabletop.business.conf.factory.punkapocalyptic;

import java.io.InputStream;
import java.util.Collection;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.ArmorDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.CollectionDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.EquipmentDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.FactionDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.SpecialRulesDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.UnitDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.ValueHandlerDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.WeaponDataType;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.WeaponEnhancementDataType;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.ArmorArmorFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.ArmorNameFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.CollectionSizeFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponCombatFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponDistanceImperialFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponDistanceMetricFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponNameFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponPenetrationFormatter;
import com.wandrell.tabletop.business.report.formatter.punkapocalyptic.WeaponStrengthFormatter;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;

public final class DynamicReportsFactory {

    private static final StyleBuilder           bold22CenteredStyle;
    private static final ComponentBuilder<?, ?> footerComponent;
    private static final DynamicReportsFactory  instance = new DynamicReportsFactory();
    private static final StyleBuilder           italicStyle;
    private static final ReportTemplateBuilder  reportTemplate;

    static {
        final StyleBuilder boldStyle;
        final StyleBuilder rootStyle;
        final StyleBuilder boldCenteredStyle;

        rootStyle = Styles.style().setPadding(2);
        boldStyle = Styles.style(rootStyle).bold();
        italicStyle = Styles.style(rootStyle).italic();

        boldCenteredStyle = Styles.style(boldStyle).setAlignment(
                HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        bold22CenteredStyle = Styles.style(boldCenteredStyle).setFontSize(22);

        footerComponent = Components.pageXofY();

        reportTemplate = DynamicReports.template();
    }

    public static final DynamicReportsFactory getInstance() {
        return instance;
    }

    private DynamicReportsFactory() {
        super();
    }

    public final DRField<Armor> getArmorArmorField(final String fieldName) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorArmorFormatter()));

        return field;
    }

    public final DRField<Armor> getArmorNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Armor> field;

        field = new DRField<Armor>(fieldName, Armor.class);
        field.setDataType(new ArmorDataType(new ArmorNameFormatter(service)));

        return field;
    }

    public final ComponentBuilder<?, ?> getAttributeCell(final String label,
            final String field) {
        return getBorderedCellComponent(Components
                .horizontalList(Components.text(label),
                        Components.text(getIntegerField(field))));
    }

    public final ComponentBuilder<?, ?> getBorderedCellComponent(
            ComponentBuilder<?, ?> content) {
        VerticalListBuilder cell;

        cell = Components.verticalList(Components.horizontalList(content));
        cell.setStyle(Styles.style(Styles.pen2Point()));

        return cell;
    }

    public final DRField<Collection<?>> getCollectionSizeField(
            final String fieldName) {
        final DRField<Collection<?>> field;

        field = new DRField<Collection<?>>(fieldName, Collection.class);
        field.setDataType(new CollectionDataType(new CollectionSizeFormatter()));

        return field;
    }

    public final SubreportBuilder getEquipmentSubreport(final String column,
            final LocalizationService service) {
        JasperReportBuilder report;

        report = DynamicReports.report();
        report.columns(DynamicReports.col.column(column, "_THIS",
                new EquipmentDataType(service)));

        return Components.subreport(report);
    }

    public final DRField<Faction> getFactionField(final String fieldName,
            final LocalizationService service) {
        final DRField<Faction> field;

        field = new DRField<Faction>(fieldName, Faction.class);
        field.setDataType(new FactionDataType(service));

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

    public final SubreportBuilder getRulesSubreport(final String column,
            final LocalizationService service) {
        JasperReportBuilder report;

        report = DynamicReports.report();
        report.columns(DynamicReports.col.column(column, "_THIS",
                new SpecialRulesDataType(service)));

        return Components.subreport(report);
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
        final ComponentBuilder<?, ?> img;
        final ComponentBuilder<?, ?> url;
        final ComponentBuilder<?, ?> title;
        final HyperLinkBuilder link;

        link = DynamicReports.hyperLink(downloadURL);
        img = Components.image(image).setFixedDimension(60, 60);
        url = Components.text(downloadURL).setStyle(italicStyle)
                .setHyperLink(link);
        title = Components.text(appName).setStyle(bold22CenteredStyle)
                .setHorizontalAlignment(HorizontalAlignment.LEFT);

        titleLabelComponent = Components.horizontalList(img,
                Components.verticalList(title, url)).setFixedWidth(300);

        return titleLabelComponent;
    }

    public final DRField<Unit> getUnitNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Unit> field;

        field = new DRField<Unit>(fieldName, Unit.class);
        field.setDataType(new UnitDataType(service));

        return field;
    }

    public final DRField<ValueBox> getValueHandlerValueField(
            final String fieldName) {
        final DRField<ValueBox> field;

        field = new DRField<ValueBox>(fieldName, ValueBox.class);
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

    public final SubreportBuilder getWeaponEnhancementsSubreport(
            final String column, final LocalizationService service) {
        JasperReportBuilder report;

        report = DynamicReports.report();
        report.columns(DynamicReports.col.column(column, "_THIS",
                new WeaponEnhancementDataType(service)));

        return Components.subreport(report);
    }

    public final DRField<Weapon> getWeaponNameField(final String fieldName,
            final LocalizationService service) {
        final DRField<Weapon> field;

        field = new DRField<Weapon>(fieldName, Weapon.class);
        field.setDataType(new WeaponDataType(new WeaponNameFormatter(service)));

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
