package com.wandrell.tabletop.business.service.file.command;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Band;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.Command;

public final class SaveBandCommand implements Command {

    private final Band band;

    public SaveBandCommand(final Band band) {
        super();

        this.band = band;
    }

    @Override
    public final void execute() {
        final Collection<Band> bands;
        final InputStream fullInput;
        final InputStream unitsInput;
        final JRBeanCollectionDataSource source;
        final Map<String, Object> parameters;
        final JasperReport fullReport;
        final JasperReport unitsReport;
        final JasperPrint print;

        try {
            fullInput = ResourceUtils
                    .getClassPathInputStream("report/punkapocalyptic-band-full-report.jrxml");
            unitsInput = ResourceUtils
                    .getClassPathInputStream("report/punkapocalyptic-unit-report.jrxml");

            bands = new LinkedList<>();
            bands.add(getBand());

            source = new JRBeanCollectionDataSource(bands);

            parameters = new HashMap<>();

            unitsReport = JasperCompileManager.compileReport(JRXmlLoader
                    .load(unitsInput));

            parameters.put("unitsReport", unitsReport);

            fullReport = JasperCompileManager.compileReport(JRXmlLoader
                    .load(fullInput));

            print = JasperFillManager
                    .fillReport(fullReport, parameters, source);
            JasperExportManager.exportReportToPdfFile(print,
                    "c:/temp/test_jasper.pdf");
        } catch (final JRException e) {
            e.printStackTrace();
        }
    }

    protected final Band getBand() {
        return band;
    }

}
