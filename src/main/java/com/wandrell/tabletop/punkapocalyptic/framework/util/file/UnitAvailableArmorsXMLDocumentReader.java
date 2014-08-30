package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.punkapocalyptic.framework.api.dao.ArmorDAO;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitAvailableArmorsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<Armor>>> {

    private final ArmorDAO daoArmor;

    public UnitAvailableArmorsXMLDocumentReader(final ArmorDAO dao) {
        super();

        daoArmor = dao;
    }

    @Override
    public final Map<String, Collection<Armor>> getValue(final Document doc) {
        final Element root;
        final Map<String, Collection<Armor>> armors;
        Element armorsNode;
        Collection<Armor> armorList;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorList = new LinkedList<>();

            armorsNode = node.getChild("armors");

            if (armorsNode != null) {
                for (final Element armor : armorsNode.getChildren()) {
                    armorList.add(getArmorDAO().getArmor(
                            armor.getChildText("name"),
                            Integer.parseInt(armor.getChildText("cost"))));
                }
            }

            armors.put(node.getChildText("unit"), armorList);
        }

        return armors;
    }

    private final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

}
