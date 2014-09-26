package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitWeaponIntervalXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllUnitWeaponIntervalsCommand implements
        ReturnCommand<Map<String, Interval>> {

    public GetAllUnitWeaponIntervalsCommand() {
        super();
    }

    @Override
    public final Map<String, Interval> execute() throws Exception {
        final ObjectParser<Map<String, Interval>> fileWeapons;
        final JDOMXMLInterpreter<Map<String, Interval>> reader;
        final JDOMXMLValidator validator;

        reader = new UnitWeaponIntervalXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileWeapons = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

}
