package com.wandrell.tabletop.util.file.punkapocalyptic.unit;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.interval.DefaultInterval;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.util.file.api.xml.XMLDocumentReader;

public final class UnitWeaponIntervalXMLDocumentReader implements
        XMLDocumentReader<Map<String, Interval>> {

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
            nodeInterval = node.getChild("constraints");
            lower = Integer.parseInt(nodeInterval.getChild("min_weapons")
                    .getValue());
            upper = Integer.parseInt(nodeInterval.getChild("max_weapons")
                    .getValue());

            interval = new DefaultInterval(lower, upper);

            weapons.put(node.getChildText("unit"), interval);
        }

        return weapons;
    }

}
