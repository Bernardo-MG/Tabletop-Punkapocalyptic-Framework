package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;

public final class UnitConstraintsXMLDocumentReader implements
        XMLDocumentReader<Map<String, Collection<ArmyBuilderUnitConstraint>>> {

    private final RulesetDAO daoRuleset;

    public UnitConstraintsXMLDocumentReader(final RulesetDAO daoRuleset) {
        super();

        this.daoRuleset = daoRuleset;
    }

    @Override
    public final Map<String, Collection<ArmyBuilderUnitConstraint>> getValue(
            final Document doc) {
        final Element root;
        final Map<String, Collection<ArmyBuilderUnitConstraint>> constraints;
        Collection<ArmyBuilderUnitConstraint> consts;
        String unit;

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

    private final Collection<ArmyBuilderUnitConstraint> getConstraints(
            final Element nodeConstraints, final String unit) {
        final Collection<ArmyBuilderUnitConstraint> constraints;

        constraints = new LinkedList<>();
        if (nodeConstraints != null) {
            for (final Element constraintNode : nodeConstraints.getChildren()) {
                constraints.add(getRulesetDAO().getUnitConstraint(
                        constraintNode.getText(), unit));
            }
        }

        return constraints;
    }

    protected final RulesetDAO getRulesetDAO() {
        return daoRuleset;
    }

}
