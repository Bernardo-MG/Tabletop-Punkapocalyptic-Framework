package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.util.file.xml.module.interpreter.XMLInterpreter;

public final class UnitInitialArmorXMLDocumentReader implements
        XMLInterpreter<Map<String, Armor>> {

    private final Map<String, Armor> armors;
    private Document                 doc;

    public UnitInitialArmorXMLDocumentReader(final Map<String, Armor> armors) {
        super();

        this.armors = armors;
    }

    @Override
    public final Map<String, Armor> getValue() {
        final Element root;
        final Map<String, Armor> armors;
        Element armorNode;

        root = getDocument().getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorNode = node.getChild(ModelNodeConf.ARMOR);

            if (armorNode != null) {
                armors.put(node.getChildText(ModelNodeConf.UNIT), getArmors()
                        .get(armorNode.getText()).createNewInstance());
            }
        }

        return armors;
    }

    public final void setDocument(final Document doc) {
        this.doc = doc;
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

    private final Document getDocument() {
        return doc;
    }

}
