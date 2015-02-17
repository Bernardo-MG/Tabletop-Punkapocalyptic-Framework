package com.wandrell.tabletop.punkapocalyptic.data.service.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseRulesCommand implements
        ReturnCommand<Collection<SpecialRule>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseRulesCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Collection<SpecialRule> execute() throws Exception {
        final Collection<SpecialRule> result;
        final Collection<Element> nodes;
        final Collection<String> names;

        nodes = XPathFactory.instance().compile("//rule", Filters.element())
                .evaluate(getDocument());

        names = new LinkedHashSet<>();
        for (final Element node : nodes) {
            names.add(node.getText());
        }

        result = new LinkedList<>();
        for (final String name : names) {
            result.add(getModelService().getSpecialRule(name));
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
