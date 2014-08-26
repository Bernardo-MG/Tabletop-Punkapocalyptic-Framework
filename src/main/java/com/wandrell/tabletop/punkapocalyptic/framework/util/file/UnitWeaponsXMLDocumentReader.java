package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitWeaponsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<String>>> {

    public UnitWeaponsXMLDocumentReader() {
        super();
    }

    @Override
    public final Map<String, Collection<String>> getValue(final Document doc) {
        final Element root;
        final Map<String, Collection<String>> weapons;
        Collection<String> weaponList;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            weaponList = new LinkedList<>();
            for (final Element weapon : node.getChild("weapons").getChildren()) {
                weaponList.add(weapon.getText());
            }
            weapons.put(node.getChildText("unit"), weaponList);
        }

        return weapons;
    }

}
