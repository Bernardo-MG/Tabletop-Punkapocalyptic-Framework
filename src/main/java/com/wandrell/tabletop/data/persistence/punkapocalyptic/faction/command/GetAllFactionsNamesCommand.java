package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionNameXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllFactionsNamesCommand implements
        ReturnCommand<Collection<String>> {

    public GetAllFactionsNamesCommand() {
        super();
    }

    @Override
    public final Collection<String> execute() {
        final FileHandler<Collection<String>> fileFactionNames;
        final XMLDocumentReader<Collection<String>> reader;
        final XMLDocumentValidator validator;

        reader = new FactionNameXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION));

        fileFactionNames = new DefaultXMLFileHandler<>(reader, validator);

        return fileFactionNames.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION));
    }

}
