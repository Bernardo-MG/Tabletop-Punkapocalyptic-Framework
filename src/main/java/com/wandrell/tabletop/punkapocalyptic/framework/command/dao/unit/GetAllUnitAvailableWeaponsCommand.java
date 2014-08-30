package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.WeaponDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.WeaponDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitAvailableWeaponsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.PathUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllUnitAvailableWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>>, WeaponDAOAware {

    private WeaponDAO daoWeapon;

    public GetAllUnitAvailableWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() {
        final FileHandler<Map<String, Collection<Weapon>>> fileUnitWeapons;
        final Map<String, Collection<Weapon>> unitWeapons;

        fileUnitWeapons = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<Weapon>>>(),
                new UnitAvailableWeaponsXMLDocumentReader(getWeaponDAO()),
                new XSDValidator(
                        PathUtils
                                .getClassPathResource(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitWeapons = fileUnitWeapons.read(PathUtils
                .getClassPathResource(ModelFile.UNIT_AVAILABILITY));

        return unitWeapons;
    }

    @Override
    public final void setWeaponDAO(final WeaponDAO dao) {
        daoWeapon = dao;
    }

    protected WeaponDAO getWeaponDAO() {
        return daoWeapon;
    }

}
