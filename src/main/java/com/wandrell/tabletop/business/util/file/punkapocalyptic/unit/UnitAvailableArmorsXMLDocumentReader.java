package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.ArmorDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

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
        Collection<Armor> armorList;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorList = getArmors(node.getChild(ModelNodeConf.ARMORS));

            armors.put(node.getChildText(ModelNodeConf.UNIT), armorList);
        }

        return armors;
    }

    private final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

    private final Collection<Armor> getArmors(final Element armorsNode) {
        final Collection<Armor> armorList;

        armorList = new LinkedList<>();
        if (armorsNode != null) {
            for (final Element armor : armorsNode.getChildren()) {
                armorList.add(getArmorDAO()
                        .getArmor(
                                armor.getChildText(ModelNodeConf.NAME),
                                Integer.parseInt(armor
                                        .getChildText(ModelNodeConf.COST))));
            }
        }

        return armorList;
    }

}
