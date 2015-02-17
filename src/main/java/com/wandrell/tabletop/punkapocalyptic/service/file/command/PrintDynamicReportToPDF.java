package com.wandrell.tabletop.punkapocalyptic.service.file.command;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import com.wandrell.pattern.command.Command;

public final class PrintDynamicReportToPDF implements Command {

    private final File                file;
    private final JasperReportBuilder report;

    public PrintDynamicReportToPDF(final JasperReportBuilder report,
            final File file) {
        super();

        this.report = report;
        this.file = file;
    }

    @Override
    public final void execute() throws DRException, IOException {
        final OutputStream stream;

        stream = new BufferedOutputStream(new FileOutputStream(file));

        report.toPdf(stream);

        stream.close();
    }

}
