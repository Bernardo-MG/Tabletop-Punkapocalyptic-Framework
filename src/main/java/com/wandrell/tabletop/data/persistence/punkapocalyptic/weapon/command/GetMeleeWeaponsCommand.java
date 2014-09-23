package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetMeleeWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>> {

    public GetMeleeWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Weapon> execute() throws Exception {
        final FileParser<Map<String, Weapon>> fileMeleeWeapons;
        final XMLAdapter<Map<String, Weapon>> adapter;
        final XMLInterpreter<Map<String, Weapon>> reader;
        final XMLValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new MeleeWeaponsXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileMeleeWeapons = new DefaultXMLFileParser<>(adapter, reader,
                validator);

        return fileMeleeWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE));
    }

}
