package com.wandrell.tabletop.util.command.punkapocalyptic.dao.faction;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.util.file.punkapocalyptic.faction.FactionUnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllFactionsUnitsCommand implements
        ReturnCommand<Map<String, Collection<String>>> {

    public GetAllFactionsUnitsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<String>> execute() {
        final FileHandler<Map<String, Collection<String>>> fileFactionUnits;
        final Map<String, Collection<String>> factionUnits;

        fileFactionUnits = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<String>>>(),
                new FactionUnitsXMLDocumentReader(),
                new XSDValidator(
                        ModelFile.VALIDATION_FACTION_UNITS,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_FACTION_UNITS)));

        factionUnits = fileFactionUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFile.FACTION_UNITS));

        return factionUnits;
    }

}
