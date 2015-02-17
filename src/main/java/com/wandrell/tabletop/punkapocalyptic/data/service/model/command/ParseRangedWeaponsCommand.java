package com.wandrell.tabletop.punkapocalyptic.data.service.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.punkapocalyptic.conf.ModelNodeConf;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.RangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.util.RangedValue;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseRangedWeaponsCommand implements
        ReturnCommand<Collection<RangedWeapon>>, ModelServiceAware {

    private final Document                document;
    private final MeleeWeapon             melee;
    private ModelService                  modelService;
    private final Repository<SpecialRule> ruleRepo;

    public ParseRangedWeaponsCommand(final Document doc,
            final Repository<SpecialRule> ruleRepo, final MeleeWeapon melee) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(melee, "Received a null pointer as melee weapon");
        checkNotNull(ruleRepo, "Received a null pointer as rules repository");

        document = doc;
        this.melee = melee;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public final Collection<RangedWeapon> execute() throws Exception {
        final Collection<RangedWeapon> weapons;
        final Collection<Element> nodes;

        nodes = XPathFactory.instance()
                .compile("//weapon_ranged_profile", Filters.element())
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

    private final MeleeWeapon getDefaultMeleeWeapon() {
        return melee;
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

    private final RangedWeapon parseNode(final Element node) {
        final Element rulesNode;
        final String name;
        final Element strength;
        final Element penetration;
        final Element range;
        final Element distanceCMNode;
        final Element distanceInchNode;
        final Integer strengthShort;
        final Integer strengthMedium;
        final Integer strengthLong;
        final Integer penetrationShort;
        final Integer penetrationMedium;
        final Integer penetrationLong;
        final Integer distanceShortCM;
        final Integer distanceMediumCM;
        final Integer distanceLongCM;
        final Integer distanceShortInches;
        final Integer distanceMediumInches;
        final Integer distanceLongInches;
        final Integer cost;
        final RangedValue distanceCM;
        final RangedValue distanceInches;
        final RangedValue strengthRanged;
        final RangedValue penetrationRanged;
        final Collection<SpecialRule> rules;

        name = node.getChildText(ModelNodeConf.NAME);

        strength = node.getChild(ModelNodeConf.STRENGTH);
        strengthShort = Integer.parseInt(strength
                .getChildText(ModelNodeConf.SHORT));
        strengthMedium = Integer.parseInt(strength
                .getChildText(ModelNodeConf.MEDIUM));
        strengthLong = Integer.parseInt(strength
                .getChildText(ModelNodeConf.LONG));

        penetration = node.getChild(ModelNodeConf.PENETRATION);
        penetrationShort = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.SHORT));
        penetrationMedium = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.MEDIUM));
        penetrationLong = Integer.parseInt(penetration
                .getChildText(ModelNodeConf.LONG));

        range = node.getChild(ModelNodeConf.RANGE);

        distanceInchNode = range.getChild(ModelNodeConf.INCHES);
        distanceShortInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.SHORT));
        distanceMediumInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.MEDIUM));
        distanceLongInches = Integer.parseInt(distanceInchNode
                .getChildText(ModelNodeConf.LONG));

        distanceCMNode = range.getChild(ModelNodeConf.CENTIMETERS);
        distanceShortCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.SHORT));
        distanceMediumCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.MEDIUM));
        distanceLongCM = Integer.parseInt(distanceCMNode
                .getChildText(ModelNodeConf.LONG));

        cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

        distanceCM = getModelService().getRangedValue(distanceShortCM,
                distanceMediumCM, distanceLongCM);
        distanceInches = getModelService().getRangedValue(distanceShortInches,
                distanceMediumInches, distanceLongInches);

        penetrationRanged = getModelService().getRangedValue(penetrationShort,
                penetrationMedium, penetrationLong);
        strengthRanged = getModelService().getRangedValue(strengthShort,
                strengthMedium, strengthLong);

        rules = new LinkedList<>();
        rulesNode = node.getChild("rules");
        if (rulesNode != null) {
            for (final Element rule : rulesNode.getChildren()) {
                rules.addAll(getRulesRepository().getCollection(
                        r -> r.getName().equals(rule.getText())));
            }
        }

        return getModelService().getRangedWeapon(name, cost, rules,
                penetrationRanged, strengthRanged, distanceCM, distanceInches,
                getDefaultMeleeWeapon());
    }

}
