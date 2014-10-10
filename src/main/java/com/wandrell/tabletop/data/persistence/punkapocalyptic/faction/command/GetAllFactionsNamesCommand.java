package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionNameParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllFactionsNamesCommand implements
        ReturnCommand<Collection<String>> {

    public GetAllFactionsNamesCommand() {
        super();
    }

    @Override
    public final Collection<String> execute() throws Exception {
        final ObjectParser<Collection<String>> fileFactionNames;
        final ParserInterpreter<Collection<String>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new FactionNameParserInterpreter();
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_FACTION,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION));

        fileFactionNames = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileFactionNames.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION));
    }

}
