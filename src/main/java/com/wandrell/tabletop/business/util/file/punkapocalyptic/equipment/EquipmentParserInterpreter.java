package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultEquipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class EquipmentParserInterpreter implements
        ParserInterpreter<Map<String, Equipment>, Document> {

    public EquipmentParserInterpreter() {
        super();
    }

    @Override
    public final Map<String, Equipment> parse(final Document doc) {
        final Element root;
        final Map<String, Equipment> equipment;
        Equipment equip;
        String name;
        Integer cost;

        checkNotNull(doc, "Received a null pointer as document");

        root = doc.getRootElement();

        equipment = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);
            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            equip = new DefaultEquipment(name, cost);

            equipment.put(name, equip);
        }

        return equipment;
    }

}
