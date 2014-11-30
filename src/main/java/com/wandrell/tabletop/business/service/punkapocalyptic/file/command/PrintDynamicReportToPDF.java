package com.wandrell.tabletop.business.service.punkapocalyptic.file.command;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import com.wandrell.util.command.Command;

public final class PrintDynamicReportToPDF implements Command {

    private final String              path;
    private final JasperReportBuilder report;

    public PrintDynamicReportToPDF(final JasperReportBuilder report,
            final String path) {
        super();

        this.report = report;
        this.path = path;
    }

    @Override
    public final void execute() throws DRException, IOException {
        final OutputStream stream;

        stream = new BufferedOutputStream(new FileOutputStream(Paths.get(path)
                .toFile()));
        report.toPdf(stream);
        stream.close();
    }

}
