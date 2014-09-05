package com.wandrell.tabletop.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.data.dao.punkapocalyptic.WeaponDAO;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitAvailableWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<Weapon>>> {

    private final WeaponDAO daoWeapon;

    public UnitAvailableWeaponsXMLDocumentReader(final WeaponDAO dao) {
        super();

        daoWeapon = dao;
    }

    @Override
    public final Map<String, Collection<Weapon>> getValue(final Document doc) {
        final Element root;
        final Map<String, Collection<Weapon>> weapons;
        Element weaponsNode;
        Collection<Weapon> weaponList;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            weaponList = new LinkedList<>();

            weaponsNode = node.getChild("weapons");
            if (weaponsNode != null) {
                for (final Element weapon : weaponsNode.getChildren()) {
                    weaponList.add(getWeaponDAO().getWeapon(weapon.getText()));
                }
            }

            weapons.put(node.getChildText("unit"), weaponList);
        }

        return weapons;
    }

    protected final WeaponDAO getWeaponDAO() {
        return daoWeapon;
    }

}
