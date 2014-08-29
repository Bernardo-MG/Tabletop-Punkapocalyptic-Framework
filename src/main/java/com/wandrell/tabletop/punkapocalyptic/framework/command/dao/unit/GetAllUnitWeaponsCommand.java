package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.wandrell.punkapocalyptic.framework.api.dao.WeaponDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.WeaponDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitWeaponsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.PathUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllUnitWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>>, WeaponDAOAware {

    private WeaponDAO daoWeapon;

    public GetAllUnitWeaponsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() {
        final FileHandler<Map<String, Collection<String>>> fileUnitWeapons;
        final Map<String, Collection<String>> unitWeaponNames;
        final Map<String, Collection<Weapon>> unitWeapons;
        Collection<Weapon> weapons;

        fileUnitWeapons = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<String>>>(),
                new UnitWeaponsXMLDocumentReader(),
                new XSDValidator(
                        PathUtils
                                .getClassPathResource(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitWeaponNames = fileUnitWeapons.read(PathUtils
                .getClassPathResource(ModelFile.UNIT_AVAILABILITY));

        unitWeapons = new LinkedHashMap<>();
        for (final Entry<String, Collection<String>> entry : unitWeaponNames
                .entrySet()) {
            weapons = new LinkedList<>();
            for (final String weapon : entry.getValue()) {
                weapons.add(getWeaponDAO().getWeapon(weapon));
            }
            unitWeapons.put(entry.getKey(), weapons);
        }

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
