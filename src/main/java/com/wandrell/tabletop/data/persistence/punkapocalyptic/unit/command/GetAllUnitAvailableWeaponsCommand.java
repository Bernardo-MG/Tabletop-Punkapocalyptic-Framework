package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableWeaponsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.WeaponDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.WeaponDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitAvailableWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>>, WeaponDAOAware {

    private WeaponDAO daoWeapon;

    public GetAllUnitAvailableWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() {
        final FileHandler<Map<String, Collection<Weapon>>> fileUnitWeapons;
        final XMLDocumentReader<Map<String, Collection<Weapon>>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitAvailableWeaponsXMLDocumentReader(getWeaponDAO());
        validator = new XSDValidator(
                ModelFileConf.UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitWeapons = new DefaultXMLFileHandler<>(reader, validator);

        return fileUnitWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    @Override
    public final void setWeaponDAO(final WeaponDAO dao) {
        daoWeapon = dao;
    }

    protected WeaponDAO getWeaponDAO() {
        return daoWeapon;
    }

}
