package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public class ParseUnitGangConstraintsCommand implements
        ReturnCommand<Map<String, UnitGangConstraint>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseUnitGangConstraintsCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, UnitGangConstraint> execute() throws Exception {
        final Map<String, UnitGangConstraint> result;
        final Collection<Element> nodes;
        UnitGangConstraint constraint;

        nodes = XPathFactory.instance()
                .compile("//faction_unit//constraint", Filters.element())
                .evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            constraint = getModelService()
                    .getUnitGangConstraint(node.getText());

            result.put(constraint.getName(), constraint);
        }

        return result;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final ModelService getModelService() {
        return modelService;
    }

}
