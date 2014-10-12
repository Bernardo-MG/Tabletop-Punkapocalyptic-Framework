package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class UnitAvailableArmorsParserInterpreter implements
        ParserInterpreter<Map<String, Collection<Armor>>, Document> {

    private final Map<String, Armor> armors;

    public UnitAvailableArmorsParserInterpreter(final Map<String, Armor> armors) {
        super();

        checkNotNull(armors, "Received a null pointer as armors");

        this.armors = armors;
    }

    @Override
    public final Map<String, Collection<Armor>> parse(final Document doc) {
        final Element root;
        final Map<String, Collection<Armor>> armors;
        Collection<Armor> armorList;

        checkNotNull(doc, "Received a null pointer as doc");

        root = doc.getRootElement();

        armors = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            armorList = getArmors(node.getChild(ModelNodeConf.ARMORS));

            armors.put(node.getChildText(ModelNodeConf.UNIT), armorList);
        }

        return armors;
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

}
