package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetMeleeWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>> {

    public GetMeleeWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Weapon> execute() throws Exception {
        final ObjectParser<Map<String, Weapon>> fileMeleeWeapons;
        final ParserInterpreter<Map<String, Weapon>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new MeleeWeaponsParserInterpreter();
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileMeleeWeapons = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileMeleeWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE));
    }

}
