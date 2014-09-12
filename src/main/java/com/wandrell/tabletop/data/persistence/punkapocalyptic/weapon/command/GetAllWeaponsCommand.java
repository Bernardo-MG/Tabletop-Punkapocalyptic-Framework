package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsXMLDocumentReader;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.RangedWeaponsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>>, RulesetDAOAware {

    private RulesetDAO daoRule;

    public GetAllWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Weapon> execute() {
        final Map<String, Weapon> weapons;

        weapons = new LinkedHashMap<>();
        weapons.putAll(getMeleeWeapons());
        weapons.putAll(getRangedWeapons());

        return weapons;
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoRule = dao;
    }

    private final Map<String, Weapon> getMeleeWeapons() {
        final FileHandler<Map<String, Weapon>> fileMeleeWeapons;
        final XMLDocumentReader<Map<String, Weapon>> reader;
        final XMLDocumentValidator validator;

        reader = new MeleeWeaponsXMLDocumentReader(getSpecialRuleDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileMeleeWeapons = new DefaultXMLFileHandler<>(reader, validator);

        return fileMeleeWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE));
    }

    private final Map<String, Weapon> getRangedWeapons() {
        final FileHandler<Map<String, Weapon>> fileRangedWeapons;
        final XMLDocumentReader<Map<String, Weapon>> reader;
        final XMLDocumentValidator validator;

        reader = new RangedWeaponsXMLDocumentReader(getSpecialRuleDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRangedWeapons = new DefaultXMLFileHandler<>(reader, validator);

        return fileRangedWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_RANGED));
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
