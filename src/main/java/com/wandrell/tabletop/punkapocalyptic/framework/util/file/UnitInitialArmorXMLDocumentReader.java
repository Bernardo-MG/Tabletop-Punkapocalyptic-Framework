package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.punkapocalyptic.framework.api.dao.ArmorDAO;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitInitialArmorXMLDocumentReader implements
        XMLDocumentReader<Map<String, Armor>> {

    private final ArmorDAO daoArmor;

    public UnitInitialArmorXMLDocumentReader(final ArmorDAO dao) {
        super();

        daoArmor = dao;
    }

    @Override
    public final Map<String, Armor> getValue(final Document doc) {
        final Element root;
        final Map<String, Armor> armors;
        Element armorNode;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorNode = node.getChild("armor");

            if (armorNode != null) {
                armors.put(node.getChildText("unit"),
                        getArmorDAO().getArmor(armorNode.getText(), 0));
            }
        }

        return armors;
    }

    private final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

}
