package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class UnitConstraintsParserInterpreter implements
        ParserInterpreter<Map<String, Collection<GangConstraint>>, Document> {

    private final Map<String, GangConstraint> constraints;

    public UnitConstraintsParserInterpreter(
            final Map<String, GangConstraint> constraints) {
        super();

        checkNotNull(constraints, "Received a null pointer as constraints");

        this.constraints = constraints;
    }

    @Override
    public final Map<String, Collection<GangConstraint>> parse(
            final Document doc) {
        final Element root;
        final Map<String, Collection<GangConstraint>> constraints;
        Collection<GangConstraint> consts;
        String unit;

        checkNotNull(doc, "Received a null pointer as doc");

        root = doc.getRootElement();

        constraints = new LinkedHashMap<>();
        for (final Element nodeFaction : root.getChildren()) {
            for (final Element nodeUnit : nodeFaction.getChild(
                    ModelNodeConf.UNITS).getChildren()) {
                unit = nodeUnit.getChildText(ModelNodeConf.NAME);

                consts = getConstraints(
                        nodeUnit.getChild(ModelNodeConf.CONSTRAINTS), unit);

                constraints.put(unit, consts);
            }
        }

        return constraints;
    }

    private final Map<String, GangConstraint> getConstraints() {
        return constraints;
    }

    private final Collection<GangConstraint> getConstraints(
            final Element nodeConstraints, final String unit) {
        final Collection<GangConstraint> constraints;
        UnitGangConstraint constraint;

        constraints = new LinkedList<>();
        if (nodeConstraints != null) {
            for (final Element constraintNode : nodeConstraints.getChildren()) {
                constraint = ((UnitGangConstraint) getConstraints().get(
                        constraintNode.getText())).createNewInstance();
                constraint.setUnit(unit);
                constraints.add(constraint);
            }
        }

        return constraints;
    }

}
