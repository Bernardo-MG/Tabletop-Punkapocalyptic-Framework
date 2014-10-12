package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableWeaponsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllUnitAvailableWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>> {

    private final Map<String, Weapon> weapons;

    public GetAllUnitAvailableWeaponsCommand(final Map<String, Weapon> weapons) {
        super();

        checkNotNull(weapons, "Received a null pointer as weapons");

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() throws Exception {
        final ObjectParser<Map<String, Collection<Weapon>>> fileUnitWeapons;
        final ParserInterpreter<Map<String, Collection<Weapon>>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitAvailableWeaponsParserInterpreter(getWeapons());
        validator = new JDOMXSDValidator(
                ModelFileConf.UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitWeapons = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileUnitWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
