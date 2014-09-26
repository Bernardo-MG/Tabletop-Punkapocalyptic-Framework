package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitInitialArmorXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllUnitsInitialArmorCommand implements
        ReturnCommand<Map<String, Armor>> {

    private final Map<String, Armor> armors;

    public GetAllUnitsInitialArmorCommand(final Map<String, Armor> armors) {
        super();

        this.armors = armors;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final ObjectParser<Map<String, Armor>> fileUnitArmors;
        final JDOMXMLInterpreter<Map<String, Armor>> reader;
        final JDOMXMLValidator validator;

        reader = new UnitInitialArmorXMLDocumentReader(getArmors());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitArmors = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    public final Map<String, Armor> getArmors() {
        return armors;
    }

}
