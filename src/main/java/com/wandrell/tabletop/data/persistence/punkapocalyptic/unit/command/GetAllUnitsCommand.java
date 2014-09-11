package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllUnitsCommand implements
        ReturnCommand<Collection<Unit>> {

    private final Map<String, Collection<SpecialRule>> rules;

    public GetAllUnitsCommand(final Map<String, Collection<SpecialRule>> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Collection<Unit> execute() {
        final FileHandler<Map<String, Unit>> fileUnits;
        final Map<String, Unit> units;

        fileUnits = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Unit>>(),
                new UnitsXMLDocumentReader(getRules()),
                new XSDValidator(ModelFile.VALIDATION_UNIT, ResourceUtils
                        .getClassPathInputStream(ModelFile.VALIDATION_UNIT)));

        units = fileUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFile.UNIT));

        return units.values();
    }

    protected final Map<String, Collection<SpecialRule>> getRules() {
        return rules;
    }

}
