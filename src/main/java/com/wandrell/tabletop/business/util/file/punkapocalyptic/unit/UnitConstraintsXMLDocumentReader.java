package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.UnitGangConstraint;
import com.wandrell.util.file.xml.module.interpreter.JDOMXMLInterpreter;

public final class UnitConstraintsXMLDocumentReader implements
        JDOMXMLInterpreter<Map<String, Collection<GangConstraint>>> {

    private final Map<String, GangConstraint> constraints;
    private Document                          doc;

    public UnitConstraintsXMLDocumentReader(
            final Map<String, GangConstraint> constraints) {
        super();

        this.constraints = constraints;
    }

    @Override
    public final Map<String, Collection<GangConstraint>> getValue() {
        final Element root;
        final Map<String, Collection<GangConstraint>> constraints;
        Collection<GangConstraint> consts;
        String unit;

        root = getDocument().getRootElement();

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

    @Override
    public final void setDocument(final Document doc) {
        this.doc = doc;
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

    private final Document getDocument() {
        return doc;
    }

}
