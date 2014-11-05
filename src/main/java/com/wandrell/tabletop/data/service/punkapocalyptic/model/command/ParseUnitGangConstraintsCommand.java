package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ConstraintsConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.util.command.ReturnCommand;

public class ParseUnitGangConstraintsCommand implements
        ReturnCommand<Map<String, UnitGangConstraint>>,
        LocalizationServiceAware {

    private final Document      document;
    private LocalizationService serviceLoc;

    public ParseUnitGangConstraintsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, UnitGangConstraint> execute() throws Exception {
        final Map<String, UnitGangConstraint> constraints;
        final Map<String, UnitGangConstraint> result;
        final Collection<Element> nodes;
        UnitGangConstraint constraint;

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

    private final Document getDocument() {
        return document;
    }

    private final LocalizationService getLocalizationService() {
        return serviceLoc;
    }

    private final UnitGangConstraint getUniqueConstraint() {
        return new UnitUpToACountConstraint("unique", 1,
                getLocalizationService());
    }

    private final UnitGangConstraint getUpToHalfPointsConstraint() {
        return new UnitUpToHalfGangLimitConstraint(getLocalizationService());
    }

    @Override
    public void setLocalizationService(final LocalizationService service) {
        serviceLoc = service;
    }

}
