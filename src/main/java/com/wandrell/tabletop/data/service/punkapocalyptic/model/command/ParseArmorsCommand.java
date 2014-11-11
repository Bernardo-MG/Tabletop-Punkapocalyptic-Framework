package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class ParseArmorsCommand implements
        ReturnCommand<Map<String, Armor>>, ModelServiceAware {

    private final Document                              document;
    private ModelService                                modelService;
    private final Map<String, ArmorInitializerModifier> modifiers;
    private final Map<String, SpecialRule>              rules;

    public ParseArmorsCommand(final Document doc,
            final Map<String, SpecialRule> rules,
            final Map<String, ArmorInitializerModifier> modifiers) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(rules, "Received a null pointer as rules");
        checkNotNull(modifiers, "Received a null pointer as modifiers");

        document = doc;
        this.rules = rules;
        this.modifiers = modifiers;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final Map<String, Armor> armors;
        final Collection<Element> nodes;
        Armor armor;

        nodes = XPathFactory.instance()
                .compile("//armor_profile", Filters.element())
                .evaluate(getDocument());

        armors = new LinkedHashMap<>();
        for (final Element node : nodes) {
            armor = parseNode(node);
            armors.put(armor.getName(), armor);
        }

        return armors;
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

    private final Map<String, ArmorInitializerModifier> getModifiers() {
        return modifiers;
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

    private final Collection<SpecialRule> getRules(final Element rulesNode) {
        Collection<SpecialRule> rules;

        rules = new LinkedList<>();
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.add(getRules().get(rule.getText()));
            }
        }

        return rules;
    }

    private final Armor parseNode(final Element node) {
        final Element modifiers;
        final String name;
        final Integer protection;
        final Collection<SpecialRule> rules;
        Armor armor;

        name = node.getChildText(ModelNodeConf.NAME);
        protection = Integer.parseInt(node
                .getChildText(ModelNodeConf.PROTECTION));
        rules = getRules(node.getChild(ModelNodeConf.RULES));

        armor = getModelService().getArmor(name, protection, rules);

        modifiers = node.getChild(ModelNodeConf.MODIFIERS);
        if (modifiers != null) {
            for (final Element modifier : modifiers.getChildren()) {
                armor = getModifiers().get(modifier.getText()).modify(armor);
            }
        }

        return armor;
    }

}
