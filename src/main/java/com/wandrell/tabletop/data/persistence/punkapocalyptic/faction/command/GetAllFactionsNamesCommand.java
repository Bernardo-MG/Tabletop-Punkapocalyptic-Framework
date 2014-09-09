package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.util.file.punkapocalyptic.faction.FactionNameXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

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
