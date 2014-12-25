package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.InputStream;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.FactionDataType;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class BuildGangReportTitleCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, ApplicationInfoServiceAware,
        LocalizationServiceAware {

    private ApplicationInfoService appInfoService;
    private final InputStream      imageStream;
    private LocalizationService    localizationService;

    public BuildGangReportTitleCommand(final InputStream imgStream) {
        super();

        imageStream = imgStream;
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final ComponentBuilder<?, ?> brand;
        final HorizontalListBuilder title;
        final DynamicReportsFactory factory;

        factory = DynamicReportsFactory.getInstance();

        brand = factory.getTitleLabelComponent(getImage(),
                getApplicationInfoService().getApplicationName(),
                getApplicationInfoService().getDownloadURI().toString());

        title = Components.horizontalList().add(brand).newRow()
                .add(Components.line());
        // Faction
        title.newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.FACTION)))
                .add(Components.text(getFactionField(ReportConf.FACTION,
                        getLocalizationService())));
        // Valoration
        title.newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.VALORATION)))
                .add(Components.text(factory
                        .getValueBoxField(ReportConf.VALORATION)));
        // Bullets
        title.newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.BULLETS)))
                .add(Components.text(factory
                        .getValueBoxField(ReportConf.BULLETS)));
        // Units
        title.newRow()
                .add(Components.text(getLocalizationService().getReportString(
                        ReportBundleConf.UNITS)))
                .add(Components.text(factory
                        .getCollectionSizeField(ReportConf.UNITS)));
        // Gap
        title.newRow().add(Components.line()).newRow()
                .add(Components.verticalGap(10));

        return title;
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

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final DRField<Faction> getFactionField(final String fieldName,
            final LocalizationService service) {
        final DRField<Faction> field;

        field = new DRField<Faction>(fieldName, Faction.class);
        field.setDataType(new FactionDataType(service));

        return field;
    }

    private final InputStream getImage() {
        return imageStream;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

}
