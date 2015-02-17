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
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.util.tag.service.ModelServiceAware;

public final class ParseFactionUnitAvailabilitiesCommand implements
        ReturnCommand<Collection<FactionUnitAvailability>>, ModelServiceAware {

    private final Document            document;
    private final Repository<Faction> factionRepo;
    private ModelService              modelService;
    private final Repository<Unit>    unitRepo;

    public ParseFactionUnitAvailabilitiesCommand(final Document doc,
            final Repository<Faction> factionRepo,
            final Repository<Unit> unitRepo) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(factionRepo,
                "Received a null pointer as factions repository");
        checkNotNull(unitRepo, "Received a null pointer as units repository");

        document = doc;
        this.unitRepo = unitRepo;
        this.factionRepo = factionRepo;
    }

    @Override
    public final Collection<FactionUnitAvailability> execute() {
        final Collection<FactionUnitAvailability> availabilities;

        availabilities = new LinkedList<>();
        for (final Faction faction : getFactionRepository().getCollection(
                f -> true)) {
            availabilities.addAll(parseNode(faction));
        }

        return availabilities;
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final Repository<Faction> getFactionRepository() {
        return factionRepo;
    }

    private final ModelService getModelService() {
        return modelService;
    }

    private final Repository<Unit> getUnitRepository() {
        return unitRepo;
    }

    private final Collection<FactionUnitAvailability> parseNode(
            final Faction faction) {
        final Collection<Element> nodes;
        final String expression;
        final Collection<FactionUnitAvailability> result;
        String expConstraint;
        Unit unit;
        Collection<Constraint> constraints;
        Collection<Element> nodesConstr;
        Constraint constr;
        String[] tags;
        Integer pos;

        expression = String.format("//faction_unit[faction='%s']/units/unit",
                faction.getName());
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        result = new LinkedList<>();
        for (final Element node : nodes) {
            unit = getUnitRepository()
                    .getCollection(
                            u -> u.getUnitName().equals(
                                    node.getChildText("name"))).iterator()
                    .next();

            expConstraint = String
                    .format("//faction_unit[faction='%s']/units/unit[name='%s']//constraint",
                            faction.getName(), unit.getUnitName());

            nodesConstr = XPathFactory.instance()
                    .compile(expConstraint, Filters.element())
                    .evaluate(getDocument());

            constraints = new LinkedList<>();
            for (final Element constraint : nodesConstr) {
                if (constraint.getChildren().size() == 1) {
                    constr = getModelService()
                            .getUnitGangConstraint(
                                    constraint.getChildText("name"),
                                    unit.getUnitName());
                } else {
                    tags = new String[constraint.getChildren().size() - 1];
                    pos = 0;
                    for (final Element tag : constraint.getChildren().subList(
                            1, constraint.getChildren().size())) {
                        tags[pos] = tag.getText();
                        pos++;
                    }
                    constr = getModelService().getUnitGangConstraint(
                            constraint.getChildText("name"),
                            unit.getUnitName(), tags);
                }

                constraints.add(constr);
            }

            result.add(getModelService().getFactionUnitAvailability(faction,
                    unit, constraints));
        }

        return result;
    }

}
