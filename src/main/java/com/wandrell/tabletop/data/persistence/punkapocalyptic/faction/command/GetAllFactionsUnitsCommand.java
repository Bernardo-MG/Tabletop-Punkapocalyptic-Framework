package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionUnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllFactionsUnitsCommand implements
        ReturnCommand<Map<String, Collection<String>>> {

    public GetAllFactionsUnitsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<String>> execute() {
        final FileHandler<Map<String, Collection<String>>> fileFactionUnits;
        final XMLDocumentReader<Map<String, Collection<String>>> reader;
        final XMLDocumentValidator validator;

        reader = new FactionUnitsXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileFactionUnits = new DefaultXMLFileHandler<>(reader, validator);

        return fileFactionUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

}
