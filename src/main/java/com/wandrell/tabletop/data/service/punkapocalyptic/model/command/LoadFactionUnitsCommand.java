package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.tabletop.business.model.punkapocalyptic.availability.DefaultFactionUnitAvailability;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.DefaultFaction;
import com.wandrell.tabletop.business.model.punkapocalyptic.faction.Faction;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.util.command.Command;

public final class LoadFactionUnitsCommand implements Command {

    private final Map<String, UnitGangConstraint> constraintsGang;
    private final Document                        document;
    private final Collection<Faction>             factions;
    private final Map<String, Unit>               units;

    public LoadFactionUnitsCommand(final Document doc,
            final Collection<Faction> factions, final Map<String, Unit> units,
            final Map<String, UnitGangConstraint> constraints) {
        super();

        checkNotNull(doc, "Received a null pointer as document");
        checkNotNull(units, "Received a null pointer as units");
        checkNotNull(constraints, "Received a null pointer as constraints");

        document = doc;
        this.units = units;
        this.factions = factions;
        constraintsGang = constraints;
    }

    @Override
    public final void execute() throws Exception {
        for (final Faction faction : getFactions()) {
            loadUnits(faction);
        }
    }

    private final Document getDocument() {
        return document;
    }

    private final Collection<Faction> getFactions() {
        return factions;
    }

    private final Map<String, UnitGangConstraint> getUnitConstraints() {
        return constraintsGang;
    }

    private final Map<String, Unit> getUnits() {
        return units;
    }

    private final void loadUnits(final Faction faction) {
        final Collection<Element> nodes;
        final String expression;
        String expConstraint;
        Unit unit;
        Collection<UnitGangConstraint> constraints;
        Collection<Element> nodesConstr;
        UnitGangConstraint constr;

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
                constr = getUnitConstraints().get(constraint.getText())
                        .createNewInstance();
                constr.setUnit(unit.getUnitName());

                constraints.add(constr);
            }

            ((DefaultFaction) faction)
                    .addUnit(new DefaultFactionUnitAvailability(unit,
                            constraints));
        }
    }

}
