package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitConstraintsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

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
        final FileParser<Map<String, Collection<GangConstraint>>> fileUnitConstraints;
        final XMLAdapter<Map<String, Collection<GangConstraint>>> adapter;
        final XMLInterpreter<Map<String, Collection<GangConstraint>>> reader;
        final XMLValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new UnitConstraintsXMLDocumentReader(getConstraints());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileUnitConstraints = new DefaultXMLFileParser<>(adapter, reader,
                validator);

        return fileUnitConstraints.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    private final Map<String, GangConstraint> getConstraints() {
        return constraints;
    }

}
