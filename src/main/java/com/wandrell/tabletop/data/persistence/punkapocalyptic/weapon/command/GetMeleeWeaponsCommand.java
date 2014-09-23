package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.FileParserUtils;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.file.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetMeleeWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>> {

    public GetMeleeWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Weapon> execute() throws Exception {
        final FileParser<Map<String, Weapon>> fileMeleeWeapons;
        final JDOMXMLInterpreter<Map<String, Weapon>> reader;
        final JDOMXMLValidator validator;

        reader = new MeleeWeaponsXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileMeleeWeapons = FileParserUtils.getJDOMFileParser(reader, validator);

        return fileMeleeWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE));
    }

}
