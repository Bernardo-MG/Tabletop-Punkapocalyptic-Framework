package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitsCommand implements
        ReturnCommand<Collection<Unit>> {

    private final Map<String, Collection<SpecialRule>> rules;

    public GetAllUnitsCommand(final Map<String, Collection<SpecialRule>> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Collection<Unit> execute() {
        final FileHandler<Collection<Unit>> fileUnits;
        final XMLDocumentReader<Collection<Unit>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitsXMLDocumentReader(getRules());
        validator = new XSDValidator(ModelFileConf.VALIDATION_UNIT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT));

        fileUnits = new DefaultXMLFileHandler<>(reader, validator);

        return fileUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT));
    }

    protected final Map<String, Collection<SpecialRule>> getRules() {
        return rules;
    }

}
