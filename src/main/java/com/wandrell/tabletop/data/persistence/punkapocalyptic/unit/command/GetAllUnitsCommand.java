package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.util.file.punkapocalyptic.unit.UnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
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
                new XSDValidator(ModelFile.VALIDATION_UNIT, ResourceUtils
                        .getClassPathInputStream(ModelFile.VALIDATION_UNIT)));

        units = fileUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFile.UNIT));

        return units.values();
    }

}
