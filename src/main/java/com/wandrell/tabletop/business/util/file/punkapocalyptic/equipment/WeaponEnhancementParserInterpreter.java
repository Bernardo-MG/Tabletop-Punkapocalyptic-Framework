package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.factory.PunkapocalypticFactory;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class WeaponEnhancementParserInterpreter implements
        ParserInterpreter<Map<String, WeaponEnhancement>, Document> {

    public WeaponEnhancementParserInterpreter() {
        super();
    }

    @Override
    public final Map<String, WeaponEnhancement> parse(final Document doc) {
        final Element root;
        final Map<String, WeaponEnhancement> enhancements;
        final PunkapocalypticFactory factory;
        WeaponEnhancement enhancement;
        String name;
        Integer cost;

        checkNotNull(doc, "Received a null pointer as document");

        root = doc.getRootElement();

        factory = PunkapocalypticFactory.getInstance();
        enhancements = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);
            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            enhancement = factory.getWeaponEnhancement(name, cost);

            enhancements.put(name, enhancement);
        }

        return enhancements;
    }

}
