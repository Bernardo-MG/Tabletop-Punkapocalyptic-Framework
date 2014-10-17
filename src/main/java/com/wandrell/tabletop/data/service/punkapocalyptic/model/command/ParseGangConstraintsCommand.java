package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ConstraintsConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public class ParseGangConstraintsCommand implements
        ReturnCommand<Map<String, GangConstraint>>, LocalizationServiceAware {

    private final Document      document;
    private LocalizationService serviceLocalization;

    public ParseGangConstraintsCommand(final Document doc) {
        super();

        document = doc;
    }

    @Override
    public final Map<String, GangConstraint> execute() throws Exception {
        final Map<String, GangConstraint> constraints;
        final Map<String, GangConstraint> result;
        final Collection<Element> nodes;
        GangConstraint constraint;

        // TODO: Use Spring
        constraints = new LinkedHashMap<>();
        constraints.put(ConstraintsConf.UNIQUE, getUniqueConstraint());
        constraints.put(ConstraintsConf.UP_TO_HALF_POINTS,
                getUpToHalfPointsConstraint());

        nodes = XPathFactory.instance()
                .compile("//faction_unit//constraint", Filters.element())
                .evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            constraint = constraints.get(node.getText());

            result.put(constraint.getName(), constraint);
        }

        return result;
    }

    @Override
    public void setLocalizationService(final LocalizationService service) {
        serviceLocalization = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final LocalizationService getLocalizationService() {
        return serviceLocalization;
    }

    private final GangConstraint getUniqueConstraint() {
        return new UnitUpToACountConstraint("unique", 1,
                getLocalizationService());
    }

    private final GangConstraint getUpToHalfPointsConstraint() {
        return new UnitUpToHalfGangLimitConstraint(getLocalizationService());
    }

}
