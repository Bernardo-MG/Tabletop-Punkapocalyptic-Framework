package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitWeaponIntervalParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllUnitWeaponIntervalsCommand implements
        ReturnCommand<Map<String, Interval>> {

    public GetAllUnitWeaponIntervalsCommand() {
        super();
    }

    @Override
    public final Map<String, Interval> execute() throws Exception {
        final ObjectParser<Map<String, Interval>> fileWeapons;
        final ParserInterpreter<Map<String, Interval>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitWeaponIntervalParserInterpreter();
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileWeapons = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

}
