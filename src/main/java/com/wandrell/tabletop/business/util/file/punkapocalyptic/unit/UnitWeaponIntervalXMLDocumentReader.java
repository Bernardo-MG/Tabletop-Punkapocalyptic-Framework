package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.interval.DefaultInterval;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.util.file.xml.module.interpreter.XMLDocumentInterpreter;

public final class UnitWeaponIntervalXMLDocumentReader implements
        XMLDocumentInterpreter<Map<String, Interval>> {

    public UnitWeaponIntervalXMLDocumentReader() {
        super();
    }

    @Override
    public final Map<String, Interval> getValue(final Document doc) {
        final Element root;
        final Map<String, Interval> weapons;
        Element nodeInterval;
        Interval interval;
        Integer lower;
        Integer upper;

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            nodeInterval = node.getChild(ModelNodeConf.CONSTRAINTS);
            lower = Integer.parseInt(nodeInterval.getChild(
                    ModelNodeConf.MIN_WEAPONS).getValue());
            upper = Integer.parseInt(nodeInterval.getChild(
                    ModelNodeConf.MAX_WEAPONS).getValue());

            interval = new DefaultInterval(lower, upper);

            weapons.put(node.getChildText(ModelNodeConf.UNIT), interval);
        }

        return weapons;
    }

}
