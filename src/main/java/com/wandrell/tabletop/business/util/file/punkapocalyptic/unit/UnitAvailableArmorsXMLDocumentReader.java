package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;

public final class UnitAvailableArmorsXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Collection<Armor>>> {

    private final Map<String, Armor> armors;
    private Document                 doc;

    public UnitAvailableArmorsXMLDocumentReader(final Map<String, Armor> armors) {
        super();

        this.armors = armors;
    }

    @Override
    public final Map<String, Collection<Armor>> getValue() {
        final Element root;
        final Map<String, Collection<Armor>> armors;
        Collection<Armor> armorList;

        root = getDocument().getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorList = getArmors(node.getChild(ModelNodeConf.ARMORS));

            armors.put(node.getChildText(ModelNodeConf.UNIT), armorList);
        }

        return armors;
    }

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

    private final Collection<Armor> getArmors(final Element armorsNode) {
        final Collection<Armor> armorList;
        Armor armr;

        armorList = new LinkedList<>();
        if (armorsNode != null) {
            for (final Element armor : armorsNode.getChildren()) {
                armr = getArmors().get(armor.getChildText(ModelNodeConf.NAME))
                        .createNewInstance();
                armr.setCost(Integer.parseInt(armor
                        .getChildText(ModelNodeConf.COST)));
                armorList.add(armr);
            }
        }

        return armorList;
    }

    private final Document getDocument() {
        return doc;
    }

}
