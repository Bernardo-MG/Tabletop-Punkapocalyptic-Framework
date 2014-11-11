package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseArmorInitializersCommand implements
        ReturnCommand<Map<String, ArmorInitializerModifier>>, ModelServiceAware {

    private final Document document;
    private ModelService   modelService;

    public ParseArmorInitializersCommand(final Document doc) {
        super();

        checkNotNull(doc, "Received a null pointer as document");

        document = doc;
    }

    @Override
    public final Map<String, ArmorInitializerModifier> execute()
            throws Exception {
        final Map<String, ArmorInitializerModifier> result;
        final Collection<Element> nodes;
        ArmorInitializerModifier modifier;

        nodes = XPathFactory
                .instance()
                .compile("//armor_profile/modifiers/modifier",
                        Filters.element()).evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            modifier = getModelService().getArmorInitializerModifier(
                    node.getText());

            result.put(modifier.getName(), modifier);
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
