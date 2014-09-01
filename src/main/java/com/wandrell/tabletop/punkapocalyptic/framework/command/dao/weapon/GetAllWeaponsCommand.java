package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.weapon;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.SpecialRuleDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.SpecialRuleDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.MeleeWeaponsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.RangedWeaponsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllWeaponsCommand implements
        ReturnCommand<Map<String, Weapon>>, SpecialRuleDAOAware {

    private SpecialRuleDAO daoRule;

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
    public final void setSpecialRuleDAO(final SpecialRuleDAO dao) {
        daoRule = dao;
    }

    protected final SpecialRuleDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
