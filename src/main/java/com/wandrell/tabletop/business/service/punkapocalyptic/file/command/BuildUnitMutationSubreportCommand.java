package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.expression.Expressions;

import com.wandrell.tabletop.business.conf.factory.punkapocalyptic.DynamicReportsFactory;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportBundleConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ReportConf;
import com.wandrell.tabletop.business.report.datatype.punkapocalyptic.MutationDataType;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class BuildUnitMutationSubreportCommand implements
        ReturnCommand<ComponentBuilder<?, ?>>, LocalizationServiceAware {

    private LocalizationService localizationService;

    public BuildUnitMutationSubreportCommand() {
        super();
    }

    @Override
    public final ComponentBuilder<?, ?> execute() {
        final SubreportBuilder subreport;

        subreport = getMutationSubreport(getLocalizationService()
                .getReportString(ReportBundleConf.MUTATIONS));
        subreport.setDataSource(Expressions
                .subDatasourceBeanCollection(ReportConf.MUTATIONS));

        return DynamicReportsFactory.getInstance().getBorderedCellComponent(
                subreport);
    }

    @Override
    public final void setLocalizationService(final LocalizationService service) {
        localizationService = service;
    }

    private final LocalizationService getLocalizationService() {
        return localizationService;
    }

    private final SubreportBuilder getMutationSubreport(final String column) {
        JasperReportBuilder report;

        report = DynamicReports.report();
        report.columns(DynamicReports.col.column(column, ReportConf.CURRENT,
                new MutationDataType(getLocalizationService())));

        return Components.subreport(report);
    }

}
