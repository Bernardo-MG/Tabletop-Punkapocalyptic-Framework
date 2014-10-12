package com.wandrell.tabletop.data.persistence.punkapocalyptic.faction.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.AvailabilityUnit;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.faction.FactionUnitsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllFactionsUnitsCommand implements
        ReturnCommand<Map<String, Collection<AvailabilityUnit>>> {

    private final Map<String, AvailabilityUnit> units;

    public GetAllFactionsUnitsCommand(final Map<String, AvailabilityUnit> units) {
        super();

        checkNotNull(units, "Received a null pointer as units");

        this.units = units;
    }

    @Override
    public final Map<String, Collection<AvailabilityUnit>> execute()
            throws Exception {
        final ObjectParser<Map<String, Collection<AvailabilityUnit>>> fileFactionUnits;
        final ParserInterpreter<Map<String, Collection<AvailabilityUnit>>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new FactionUnitsParserInterpreter(getUnits());
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileFactionUnits = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileFactionUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    private final Map<String, AvailabilityUnit> getUnits() {
        return units;
    }

}
