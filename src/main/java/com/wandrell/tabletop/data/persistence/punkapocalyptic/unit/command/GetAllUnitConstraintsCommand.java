package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.UnitConstraint;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitConstraintsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllUnitConstraintsCommand implements
        ReturnCommand<Map<String, Collection<UnitConstraint>>>, RulesetDAOAware {

    private RulesetDAO daoRuleset;

    public GetAllUnitConstraintsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<UnitConstraint>> execute() {
        final Map<String, Collection<UnitConstraint>> constraints;
        final Map<String, Collection<String>> constraintNames;
        final FileHandler<Map<String, Collection<String>>> fileUnitConstraints;
        Collection<UnitConstraint> consts;

        fileUnitConstraints = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<String>>>(),
                new UnitConstraintsXMLDocumentReader(),
                new XSDValidator(
                        ModelFile.VALIDATION_FACTION_UNITS,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_FACTION_UNITS)));

        constraintNames = fileUnitConstraints.read(ResourceUtils
                .getClassPathInputStream(ModelFile.FACTION_UNITS));

        constraints = new LinkedHashMap<>();
        for (final Entry<String, Collection<String>> entry : constraintNames
                .entrySet()) {
            consts = new LinkedList<>();
            for (final String constraint : entry.getValue()) {
                consts.add(getRulesetDAO().getUnitConstraint(constraint,
                        entry.getKey()));
            }
            constraints.put(entry.getKey(), consts);
        }

        return constraints;
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoRuleset = dao;
    }

    protected final RulesetDAO getRulesetDAO() {
        return daoRuleset;
    }

}
