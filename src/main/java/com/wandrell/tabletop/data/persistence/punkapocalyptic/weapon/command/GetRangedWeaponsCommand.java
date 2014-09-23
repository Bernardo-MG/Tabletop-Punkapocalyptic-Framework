package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsXMLDocumentReader;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.RangedWeaponsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.FileParserUtils;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.file.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetRangedWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>> {

    private final Map<String, Weapon> weapons;

    public GetRangedWeaponsCommand(final Map<String, Weapon> weapons) {
        super();

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Weapon> execute() throws Exception {
        final Map<String, Weapon> weapons;

        weapons = new LinkedHashMap<>();
        weapons.putAll(getMeleeWeapons());
        weapons.putAll(getRangedWeapons());

        return weapons;
    }

    private final Map<String, Weapon> getMeleeWeapons() throws Exception {
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

    private final Map<String, Weapon> getRangedWeapons() throws Exception {
        final FileParser<Map<String, Weapon>> fileRangedWeapons;
        final JDOMXMLInterpreter<Map<String, Weapon>> reader;
        final JDOMXMLValidator validator;

        reader = new RangedWeaponsXMLDocumentReader((MeleeWeapon) getWeapons()
                .get(WeaponNameConf.LIGHT_MACE));
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRangedWeapons = FileParserUtils
                .getJDOMFileParser(reader, validator);

        return fileRangedWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_RANGED));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
