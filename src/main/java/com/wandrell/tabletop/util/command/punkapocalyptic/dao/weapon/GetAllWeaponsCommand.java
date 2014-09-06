package com.wandrell.tabletop.util.command.punkapocalyptic.dao.weapon;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.data.dao.punkapocalyptic.RulesetDAO;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.util.file.punkapocalyptic.equipment.MeleeWeaponsXMLDocumentReader;
import com.wandrell.tabletop.util.file.punkapocalyptic.equipment.RangedWeaponsXMLDocumentReader;
import com.wandrell.tabletop.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>>, RulesetDAOAware {

    private RulesetDAO daoRule;

    public GetAllWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Weapon> execute() {
        final FileHandler<Map<String, Weapon>> fileCCWeapons;
        final Map<String, Weapon> ccWeapons;
        final FileHandler<Map<String, Weapon>> fileRangedWeapons;
        final Map<String, Weapon> rangedWeapons;
        final Map<String, Weapon> weapons;

        fileCCWeapons = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Weapon>>(),
                new MeleeWeaponsXMLDocumentReader(getSpecialRuleDAO()),
                new XSDValidator(
                        ModelFile.VALIDATION_WEAPON_MELEE,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_WEAPON_MELEE)));

        ccWeapons = fileCCWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFile.WEAPON_MELEE));

        fileRangedWeapons = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Weapon>>(),
                new RangedWeaponsXMLDocumentReader(getSpecialRuleDAO()),
                new XSDValidator(
                        ModelFile.VALIDATION_WEAPON_RANGED,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_WEAPON_RANGED)));

        rangedWeapons = fileRangedWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFile.WEAPON_RANGED));

        weapons = new LinkedHashMap<>();
        weapons.putAll(ccWeapons);
        weapons.putAll(rangedWeapons);

        return weapons;
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoRule = dao;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
