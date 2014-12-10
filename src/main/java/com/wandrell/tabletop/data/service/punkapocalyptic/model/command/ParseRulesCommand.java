package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseRulesCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, SpecialRule> execute() throws Exception {
        final Map<String, SpecialRule> result;
        final Collection<Element> nodes;
        final Collection<String> names;
        SpecialRule rule;

        nodes = XPathFactory.instance().compile("//rule", Filters.element())
                .evaluate(getDocument());

        names = new LinkedHashSet<>();
        for (final Element node : nodes) {
            names.add(node.getText());
        }

        result = new LinkedHashMap<>();
        for (final String name : names) {
            rule = getModelService().getSpecialRule(name);

            result.put(rule.getName(), rule);
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
