package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableWeaponsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.WeaponDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.WeaponDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

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
                        ModelFile.UNIT_AVAILABILITY,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitWeapons = fileUnitWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFile.UNIT_AVAILABILITY));

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
