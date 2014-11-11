package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.ModelService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ModelServiceAware;
import com.wandrell.util.command.Command;

public final class LoadFactionUnitsCommand implements Command,
        ModelServiceAware {

    private final Document            document;
    private final Collection<Faction> factions;
    private ModelService              modelService;
    private final Map<String, Unit>   units;

    public LoadFactionUnitsCommand(final Document doc,
            final Collection<Faction> factions, final Map<String, Unit> units) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(factions, "Received a null pointer as factions");
        checkNotNull(units, "Received a null pointer as units");

        document = doc;
        this.units = units;
        this.factions = factions;
    }

    @Override
    public final void execute() throws Exception {
        for (final Faction faction : getFactions()) {
            loadUnits(faction);
        }
    }

    @Override
    public final void setModelService(final ModelService service) {
        modelService = service;
    }

    private final Document getDocument() {
        return document;
    }

    private final Collection<Faction> getFactions() {
        return factions;
    }

    private final ModelService getModelService() {
        return modelService;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

    private final void loadUnits(final Faction faction) {
        final Collection<Element> nodes;
        final String expression;
        String expConstraint;
        Unit unit;
        Collection<GangConstraint> constraints;
        Collection<Element> nodesConstr;
        GangConstraint constr;

        expression = String.format("//faction_unit[faction='%s']//unit",
                faction.getName());
        nodes = XPathFactory.instance().compile(expression, Filters.element())
                .evaluate(getDocument());

        for (final Element node : nodes) {
            unit = getUnits().get(node.getChildText("name"));

            expConstraint = String
                    .format("//faction_unit[faction='%s']//unit[name='%s']//constraint",
                            faction.getName(), unit.getUnitName());

            nodesConstr = XPathFactory.instance()
                    .compile(expConstraint, Filters.element())
                    .evaluate(getDocument());

            constraints = new LinkedList<>();
            for (final Element constraint : nodesConstr) {
                constr = getModelService().getUnitGangConstraint(
                        constraint.getText(), unit.getUnitName());

                constraints.add(constr);
            }

            faction.addUnit(getModelService().getFactionUnitAvailability(unit,
                    constraints));
        }
    }

}
