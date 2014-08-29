package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.unit.Unit;
import com.wandrell.util.PathUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllUnitsCommand implements
        ReturnCommand<Collection<Unit>> {

    public GetAllUnitsCommand() {
        super();
    }

    @Override
    public final Collection<Unit> execute() {
        final FileHandler<Map<String, Unit>> fileUnits;
        final Map<String, Unit> units;

        fileUnits = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Unit>>(),
                new UnitsXMLDocumentReader(),
                new XSDValidator(PathUtils
                        .getClassPathResource(ModelFile.VALIDATION_UNIT)));

        units = fileUnits.read(PathUtils.getClassPathResource(ModelFile.UNIT));

        return units.values();
    }

}
