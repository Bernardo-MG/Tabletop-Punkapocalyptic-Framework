package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsParserInterpreter;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.RangedWeaponsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetRangedWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>> {

    private final Map<String, Weapon> weapons;

    public GetRangedWeaponsCommand(final Map<String, Weapon> weapons) {
        super();

        checkNotNull(weapons, "Received a null pointer as weapons");

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

    private final Map<String, Weapon> getRangedWeapons() throws Exception {
        final ObjectParser<Map<String, Weapon>> fileRangedWeapons;
        final ParserInterpreter<Map<String, Weapon>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new RangedWeaponsParserInterpreter((MeleeWeapon) getWeapons()
                .get(WeaponNameConf.LIGHT_MACE));
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRangedWeapons = ParserFactory.getInstance().getJDOMXMLParser(
                reader, validator);

        return fileRangedWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_RANGED));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
