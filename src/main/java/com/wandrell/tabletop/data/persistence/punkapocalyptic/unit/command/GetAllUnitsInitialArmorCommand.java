package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitInitialArmorParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

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
        final ParserInterpreter<Map<String, Armor>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitInitialArmorParserInterpreter(getArmors());
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitArmors = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    public final Map<String, Armor> getArmors() {
        return armors;
    }

}
