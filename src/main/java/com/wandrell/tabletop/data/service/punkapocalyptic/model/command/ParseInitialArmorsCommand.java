package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.util.command.ReturnCommand;

public final class ParseInitialArmorsCommand implements
        ReturnCommand<Map<String, Armor>> {

    private final Map<String, Armor> armors;
    private final Document           document;

    public ParseInitialArmorsCommand(final Document doc,
            final Map<String, Armor> armors) {
        super();

        document = doc;
        this.armors = armors;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final Map<String, Armor> armors;
        final Collection<Element> nodes;
        Element armorNode;

        nodes = XPathFactory.instance()
                .compile("//unit_armor", Filters.element())
                .evaluate(getDocument());

        armors = new LinkedHashMap<>();
        for (final Element node : nodes) {
            armorNode = node.getChild(ModelNodeConf.ARMOR);

            if (armorNode != null) {
                armors.put(node.getChildText(ModelNodeConf.UNIT), getArmors()
                        .get(armorNode.getText()).createNewInstance());
            }
        }

        return armors;
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

    private final Document getDocument() {
        return document;
    }

}
