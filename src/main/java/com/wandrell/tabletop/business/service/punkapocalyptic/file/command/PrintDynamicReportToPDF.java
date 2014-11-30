package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import com.wandrell.util.command.Command;

public final class PrintDynamicReportToPDF implements Command {

    private final JasperReportBuilder report;
    private final OutputStream        stream;

    public PrintDynamicReportToPDF(final JasperReportBuilder report,
            final OutputStream stream) {
        super();

        this.report = report;
        this.stream = stream;
    }

    @Override
    public final void execute() throws DRException, IOException {
        report.toPdf(stream);
        stream.close();
    }

}
