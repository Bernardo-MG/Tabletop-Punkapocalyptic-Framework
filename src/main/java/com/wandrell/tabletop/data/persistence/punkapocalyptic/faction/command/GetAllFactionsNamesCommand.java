package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionNameXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllFactionsNamesCommand implements
        ReturnCommand<Collection<String>> {

    public GetAllFactionsNamesCommand() {
        super();
    }

    @Override
    public final Collection<String> execute() throws Exception {
        final ObjectParser<Collection<String>> fileFactionNames;
        final JDOMXMLInterpreter<Collection<String>> reader;
        final JDOMXMLValidator validator;

        reader = new FactionNameXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION));

        fileFactionNames = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileFactionNames.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION));
    }

}
