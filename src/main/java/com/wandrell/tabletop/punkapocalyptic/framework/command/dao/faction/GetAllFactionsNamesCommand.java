package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.faction;

import java.util.Collection;

import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.FactionNameXMLDocumentReader;
import com.wandrell.util.PathUtils;
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
                new XSDValidator(PathUtils
                        .getClassPathResource(ModelFile.VALIDATION_FACTION)));

        factionNames = fileFactionNames.read(PathUtils
                .getClassPathResource(ModelFile.FACTION));

        return factionNames;
    }

}
