package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionNameXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllFactionsNamesCommand implements
        ReturnCommand<Collection<String>> {

    public GetAllFactionsNamesCommand() {
        super();
    }

    @Override
    public final Collection<String> execute() {
        final FileHandler<Collection<String>> fileFactionNames;
        final Collection<String> factionNames;

        fileFactionNames = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Collection<String>>(),
                new FactionNameXMLDocumentReader(),
                new XSDValidator(ModelFile.VALIDATION_FACTION, ResourceUtils
                        .getClassPathInputStream(ModelFile.VALIDATION_FACTION)));

        factionNames = fileFactionNames.read(ResourceUtils
                .getClassPathInputStream(ModelFile.FACTION));

        return factionNames;
    }

}
