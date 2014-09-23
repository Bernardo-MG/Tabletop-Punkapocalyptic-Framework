package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;

public final class UnitAvailableWeaponsXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Collection<Weapon>>> {

    private Document                  doc;
    private final Map<String, Weapon> weapons;

    public UnitAvailableWeaponsXMLDocumentReader(
            final Map<String, Weapon> weapons) {
        super();

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Collection<Weapon>> getValue() {
        final Element root;
        final Map<String, Collection<Weapon>> weapons;
        Collection<Weapon> weaponList;

        root = getDocument().getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            weaponList = getWeapons(node.getChild(ModelNodeConf.WEAPONS));

            weapons.put(node.getChildText(ModelNodeConf.UNIT), weaponList);
        }

        return weapons;
    }

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Document getDocument() {
        return doc;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

    private final Collection<Weapon> getWeapons(final Element weaponsNode) {
        final Collection<Weapon> weaponList;

        weaponList = new LinkedList<>();
        if (weaponsNode != null) {
            for (final Element weapon : weaponsNode.getChildren()) {
                weaponList.add(getWeapons().get(weapon.getText()));
            }
        }

        return weaponList;
    }

}
