package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModifiersConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.BulletproofArmorInitializerModifier;
import com.wandrell.util.command.ReturnCommand;

public final class ParseArmorInitializersCommand implements
        ReturnCommand<Map<String, ArmorInitializerModifier>> {

    private final Document document;

    public ParseArmorInitializersCommand(final Document doc) {
        super();

        document = doc;
    }

    @Override
    public final Map<String, ArmorInitializerModifier> execute()
            throws Exception {
        final Map<String, ArmorInitializerModifier> modifiers;
        final Map<String, ArmorInitializerModifier> result;
        final Collection<Element> nodes;
        ArmorInitializerModifier modifier;

        // TODO: Use Spring
        modifiers = new LinkedHashMap<>();
        modifiers.put(ModifiersConf.FIREARMS_PROT,
                new BulletproofArmorInitializerModifier());

        nodes = XPathFactory
                .instance()
                .compile("//armor_profile/modifiers/modifier",
                        Filters.element()).evaluate(getDocument());

        result = new LinkedHashMap<>();
        for (final Element node : nodes) {
            modifier = modifiers.get(node.getText());

            result.put(modifier.getName(), modifier);
        }

        return result;
    }

    private final Document getDocument() {
        return document;
    }

}
