package com.wandrell.tabletop.punkapocalyptic.framework.util.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitArmorsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<Collection<Object>>>> {

    public UnitArmorsXMLDocumentReader() {
        super();
    }

    @Override
    public final Map<String, Collection<Collection<Object>>> getValue(
            final Document doc) {
        final Element root;
        final Map<String, Collection<Collection<Object>>> armors;
        Element armorsNode;
        Collection<Collection<Object>> armorList;
        Collection<Object> data;

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorList = new LinkedList<>();

            armorsNode = node.getChild("armors");

            if (armorsNode != null) {
                for (final Element armor : armorsNode.getChildren()) {
                    data = new LinkedList<>();
                    data.add(armor.getChildText("name"));
                    data.add(Integer.parseInt(armor.getChildText("cost")));

                    armorList.add(data);
                }
            }

            armors.put(node.getChildText("unit"), armorList);
        }

        return armors;
    }

}
