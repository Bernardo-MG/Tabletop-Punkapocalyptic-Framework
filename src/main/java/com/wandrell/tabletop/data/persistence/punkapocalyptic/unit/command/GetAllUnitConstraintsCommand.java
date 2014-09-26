package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitConstraintsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllUnitConstraintsCommand implements
        ReturnCommand<Map<String, Collection<GangConstraint>>> {

    private final Map<String, GangConstraint> constraints;

    public GetAllUnitConstraintsCommand(
            final Map<String, GangConstraint> constraints) {
        super();

        this.constraints = constraints;
    }

    @Override
    public final Map<String, Collection<GangConstraint>> execute()
            throws Exception {
        final ObjectParser<Map<String, Collection<GangConstraint>>> fileUnitConstraints;
        final JDOMXMLInterpreter<Map<String, Collection<GangConstraint>>> reader;
        final JDOMXMLValidator validator;

        reader = new UnitConstraintsXMLDocumentReader(getConstraints());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileUnitConstraints = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileUnitConstraints.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    private final Map<String, GangConstraint> getConstraints() {
        return constraints;
    }

}
