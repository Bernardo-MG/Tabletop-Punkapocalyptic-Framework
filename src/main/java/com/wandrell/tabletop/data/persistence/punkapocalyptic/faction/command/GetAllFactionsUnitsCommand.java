package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionUnitsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllFactionsUnitsCommand implements
        ReturnCommand<Map<String, Collection<AvailabilityUnit>>> {

    private final Map<String, AvailabilityUnit> units;

    public GetAllFactionsUnitsCommand(final Map<String, AvailabilityUnit> units) {
        super();

        this.units = units;
    }

    @Override
    public final Map<String, Collection<AvailabilityUnit>> execute()
            throws Exception {
        final FileParser<Map<String, Collection<AvailabilityUnit>>> fileFactionUnits;
        final XMLDocumentReader<Map<String, Collection<AvailabilityUnit>>> reader;
        final XMLDocumentValidator validator;

        reader = new FactionUnitsXMLDocumentReader(getUnits());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileFactionUnits = new DefaultXMLFileParser<>(reader, validator);

        return fileFactionUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    private final Map<String, AvailabilityUnit> getUnits() {
        return units;
    }

}
