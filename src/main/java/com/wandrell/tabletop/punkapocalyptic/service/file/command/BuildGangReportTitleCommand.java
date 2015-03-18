package com.wandrell.tabletop.punkapocalyptic.service.file.command;

import java.io.InputStream;

import net.sf.dynamicreports.report.base.DRField;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.pattern.service.application.ApplicationInfoService;
import com.wandrell.tabletop.punkapocalyptic.conf.ReportConf;
import com.wandrell.tabletop.punkapocalyptic.conf.factory.DynamicReportsFactory;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.FactionDataType;
import com.wandrell.tabletop.punkapocalyptic.report.datatype.GangDataType;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.FactionNameFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangBulletsFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangUnitsRangeFormatter;
import com.wandrell.tabletop.punkapocalyptic.report.formatter.GangValorationFormatter;
import com.wandrell.tabletop.punkapocalyptic.service.FileService;
import com.wandrell.tabletop.punkapocalyptic.service.LocalizationService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.FileServiceAware;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.LocalizationServiceAware;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.RulesetServiceAware;
import com.wandrell.util.ResourceUtils;

public final class BuildGangReportTitleCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, ApplicationInfoServiceAware,
        FileServiceAware, LocalizationServiceAware, RulesetServiceAware {

    private ApplicationInfoService appInfoService;
    private FileService            fileService;
    private LocalizationService    localizationService;
    private RulesetService         rulesetService;

    public BuildGangReportTitleCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final ComponentBuilder<?, ?> brand;
        final VerticalListBuilder gangData;
        final DynamicReportsFactory factory;
        final InputStream imageStream;

        imageStream = ResourceUtils.getClassPathInputStream(getFileService()
                .getTitleImagePath());

        factory = DynamicReportsFactory.getInstance();

        brand = factory.getTitleLabelComponent(imageStream,
                getApplicationInfoService().getApplicationName(),
                getApplicationInfoService().getVersion(),
                getApplicationInfoService().getDownloadURI().toString());
        brand.setStyle(Styles.style().setRightBorder(Styles.pen1Point()));

        gangData = Components.verticalList();
        // Faction
        gangData.add(Components.text(getFactionNameField(ReportConf.FACTION))
                .setStyle(factory.getTitleStyle()));
        // Valoration
        gangData.add(Components
                .text(getGangValorationField(ReportConf.CURRENT)));
        // Units
        gangData.add(Components.text(getGangUnitsField(ReportConf.CURRENT)));

        // Bullets
        gangData.add(Components.text(getGangBulletsField(ReportConf.CURRENT)));

        return Components.verticalList().add(
                Components.horizontalList().add(brand,
                        Components.horizontalGap(5), gangData),
                Components.line(), Components.verticalGap(10));
    }

    @Override
    public final void setApplicationInfoService(
            final ApplicationInfoService service) {
        appInfoService = service;
    }

    @Override
    public final void setFileService(final FileService service) {
        fileService = service;
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    @Override
    public final void setRulesetService(final RulesetService service) {
        rulesetService = service;
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return appInfoService;
    }

    private final DRField<Faction> getFactionNameField(final String fieldName) {
        final DRField<Faction> field;

        field = new DRField<Faction>(fieldName, Faction.class);
        field.setDataType(new FactionDataType(new FactionNameFormatter()));

        return field;
    }

    private final FileService getFileService() {
        return fileService;
    }

    private final DRField<Gang> getGangBulletsField(final String fieldName) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangBulletsFormatter(
                getLocalizationService())));

        return field;
    }

    private final DRField<Gang> getGangUnitsField(final String fieldName) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangUnitsRangeFormatter(
                getLocalizationService(), getRulesetService())));

        return field;
    }

    private final DRField<Gang> getGangValorationField(final String fieldName) {
        final DRField<Gang> field;

        field = new DRField<Gang>(fieldName, Gang.class);
        field.setDataType(new GangDataType(new GangValorationFormatter(
                getLocalizationService())));

        return field;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

}
