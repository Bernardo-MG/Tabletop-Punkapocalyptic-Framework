package com.wandrell.tabletop.business.conf.factory.punkapocalyptic;

import java.io.InputStream;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.ValueBoxDataType;

public final class DynamicReportsFactory {

    private static final StyleBuilder           bold22Style;
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
                HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
        bold22Style = Styles.style(boldCenteredStyle).setFontSize(22);

        footerComponent = Components.pageXofY();

        reportTemplate = DynamicReports.template();
    }

    public static final DynamicReportsFactory getInstance() {
        return instance;
    }

    private DynamicReportsFactory() {
        super();
    }

    public final ComponentBuilder<?, ?> getBorderedCellComponent(
            final ComponentBuilder<?, ?> content) {
        final VerticalListBuilder cell;

        cell = Components.verticalList(Components.horizontalList(content));
        cell.setStyle(Styles.style(Styles.pen1Point()));

        return cell;
    }

    public final ComponentBuilder<?, ?> getReportFooter() {
        return footerComponent;
    }

    public final ReportTemplateBuilder getReportTemplate() {
        return reportTemplate;
    }

    public final DRField<String> getStringField(final String fieldName) {
        final DRField<String> field;

        field = new DRField<String>(fieldName, String.class);
        field.setDataType(DynamicReports.type.stringType());

        return field;
    }

    public final ComponentBuilder<?, ?> getTitleLabelComponent(
            final InputStream image, final String appName,
            final String appVersion, final String downloadURL) {
        final ComponentBuilder<?, ?> titleLabelComponent;
        final ComponentBuilder<?, ?> img;
        final ComponentBuilder<?, ?> url;
        final ComponentBuilder<?, ?> title;
        final HyperLinkBuilder link;
        final String titleLabel;

        titleLabel = String.format("%s v%s", appName, appVersion);

        link = DynamicReports.hyperLink(downloadURL);
        img = Components.image(image).setFixedDimension(200, 50);
        url = Components.text(downloadURL).setStyle(italicStyle)
                .setHyperLink(link);
        title = Components.text(titleLabel).setStyle(getTitleStyle());

        titleLabelComponent = Components.verticalList(img, title, url)
                .setFixedWidth(250);

        return titleLabelComponent;
    }

    public final StyleBuilder getTitleStyle() {
        return bold22Style;
    }

    public final DRField<ValueBox> getValueBoxField(final String fieldName) {
        final DRField<ValueBox> field;

        field = new DRField<ValueBox>(fieldName, ValueBox.class);
        field.setDataType(new ValueBoxDataType());

        return field;
    }

}
