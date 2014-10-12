package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitConstraintsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllUnitConstraintsCommand implements
        ReturnCommand<Map<String, Collection<GangConstraint>>> {

    private final Map<String, GangConstraint> constraints;

    public GetAllUnitConstraintsCommand(
            final Map<String, GangConstraint> constraints) {
        super();

        checkNotNull(constraints, "Received a null pointer as constraints");

        this.constraints = constraints;
    }

    @Override
    public final Map<String, Collection<GangConstraint>> execute()
            throws Exception {
        final ObjectParser<Map<String, Collection<GangConstraint>>> fileUnitConstraints;
        final ParserInterpreter<Map<String, Collection<GangConstraint>>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitConstraintsParserInterpreter(getConstraints());
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileUnitConstraints = ParserFactory.getInstance().getJDOMXMLParser(
                reader, validator);

        return fileUnitConstraints.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    private final Map<String, GangConstraint> getConstraints() {
        return constraints;
    }

}
