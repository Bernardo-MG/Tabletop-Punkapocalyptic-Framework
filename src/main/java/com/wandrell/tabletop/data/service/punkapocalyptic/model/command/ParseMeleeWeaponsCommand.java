package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.repository.Repository;

public final class ParseMeleeWeaponsCommand implements
        ReturnCommand<Collection<MeleeWeapon>>, ModelServiceAware {

    private final Document                document;
    private ModelService                  modelService;
    private final Repository<SpecialRule> ruleRepo;

    public ParseMeleeWeaponsCommand(final Document doc,
            final Repository<SpecialRule> ruleRepo) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(ruleRepo, "Received a null pointer as rules repository");

        document = doc;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public final Collection<MeleeWeapon> execute() throws Exception {
        final Collection<MeleeWeapon> weapons;
        final Collection<Element> nodes;

        nodes = XPathFactory.instance()
                .compile("//weapon_melee_profile", Filters.element())
                .evaluate(getDocument());

        weapons = new LinkedList<>();
        for (final Element node : nodes) {
            weapons.add(parseNode(node));
        }

        return weapons;
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

    private final Repository<SpecialRule> getRulesRepository() {
        return ruleRepo;
    }

    private final MeleeWeapon parseNode(final Element node) {
        final Element rulesNode;
        final String name;
        final Integer strength;
        final Integer penetration;
        final Integer combat;
        final Integer cost;
        final MeleeWeapon weapon;
        final Collection<SpecialRule> rules;

        name = node.getChildText(ModelNodeConf.NAME);
        strength = Integer.parseInt(node.getChildText(ModelNodeConf.STRENGTH));
        penetration = Integer.parseInt(node
                .getChildText(ModelNodeConf.PENETRATION));
        combat = Integer.parseInt(node.getChildText(ModelNodeConf.COMBAT));
        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        rules = new LinkedList<>();
        rulesNode = node.getChild("rules");
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.addAll(getRulesRepository().getCollection(
                        r -> r.getName().equals(rule.getText())));
            }
        }

        weapon = getModelService().getMeleeWeapon(name, cost, strength,
                penetration, combat, rules);

        return weapon;
    }

}
