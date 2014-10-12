package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableArmorsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllUnitAvailableArmorsCommand implements
        ReturnCommand<Map<String, Collection<Armor>>> {

    private final Map<String, Armor> armors;

    public GetAllUnitAvailableArmorsCommand(final Map<String, Armor> armors) {
        super();

        checkNotNull(armors, "Received a null pointer as armors");

        this.armors = armors;
    }

    @Override
    public final Map<String, Collection<Armor>> execute() throws Exception {
        final ObjectParser<Map<String, Collection<Armor>>> fileUnitArmors;
        final ParserInterpreter<Map<String, Collection<Armor>>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitAvailableArmorsParserInterpreter(getArmors());
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitArmors = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

}
