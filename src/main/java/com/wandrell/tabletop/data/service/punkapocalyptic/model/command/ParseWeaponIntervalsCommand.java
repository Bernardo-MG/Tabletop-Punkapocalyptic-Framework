package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.interval.DefaultInterval;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.util.command.ReturnCommand;

public final class ParseWeaponIntervalsCommand implements
        ReturnCommand<Map<String, Interval>> {

    private final Document document;

    public ParseWeaponIntervalsCommand(final Document doc) {
        super();

        document = doc;
    }

    @Override
    public final Map<String, Interval> execute() throws Exception {
        final Map<String, Interval> intervals;
        final Collection<Element> nodes;
        Element intervalNode;
        Interval interval;
        Integer lower;
        Integer upper;

        nodes = XPathFactory.instance()
                .compile("//unit_weapon", Filters.element())
                .evaluate(getDocument());

        intervals = new LinkedHashMap<>();
        for (final Element node : nodes) {
            intervalNode = node.getChild("weapons_interval");

            lower = Integer.parseInt(intervalNode.getChild(
                    ModelNodeConf.MIN_WEAPONS).getValue());
            upper = Integer.parseInt(intervalNode.getChild(
                    ModelNodeConf.MAX_WEAPONS).getValue());

            interval = new DefaultInterval(lower, upper);

            intervals.put(node.getChildText(ModelNodeConf.UNIT), interval);
        }

        return intervals;
    }

    private final Document getDocument() {
        return document;
    }

}
