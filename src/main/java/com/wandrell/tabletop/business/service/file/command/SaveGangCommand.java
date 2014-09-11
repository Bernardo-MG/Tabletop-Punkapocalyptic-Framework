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

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.Command;

public final class SaveGangCommand implements Command {

    private final Gang band;

    public SaveGangCommand(final Gang band) {
        super();

        this.band = band;
    }

    @Override
    public final void execute() {
        final Collection<Gang> bands;
        final InputStream fullInput;
        final InputStream weaponsInput;
        final InputStream unitsInput;
        final JRBeanCollectionDataSource source;
        final Map<String, Object> parameters;
        final Map<String, Object> parametersUnits;
        final JasperReport fullReport;
        final JasperReport unitsReport;
        final JasperReport weaponsReport;
        final JasperPrint print;

        try {
            fullInput = ResourceUtils
                    .getClassPathInputStream("report/punkapocalyptic-gang-report.jrxml");
            unitsInput = ResourceUtils
                    .getClassPathInputStream("report/punkapocalyptic-unit-report.jrxml");
            weaponsInput = ResourceUtils
                    .getClassPathInputStream("report/punkapocalyptic-weapon-report.jrxml");

            bands = new LinkedList<>();
            bands.add(getBand());

            source = new JRBeanCollectionDataSource(bands);

            parameters = new HashMap<>();

            unitsReport = JasperCompileManager.compileReport(JRXmlLoader
                    .load(unitsInput));
            weaponsReport = JasperCompileManager.compileReport(JRXmlLoader
                    .load(weaponsInput));

            parameters.put("unitsReport", unitsReport);
            parameters.put("weaponsReport", weaponsReport);

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

    protected final Gang getBand() {
        return band;
    }

}
