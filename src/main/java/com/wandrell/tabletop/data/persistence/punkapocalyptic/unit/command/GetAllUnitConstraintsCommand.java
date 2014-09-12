package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitConstraintsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitConstraintsCommand implements
        ReturnCommand<Map<String, Collection<ArmyBuilderUnitConstraint>>>,
        RulesetDAOAware {

    private RulesetDAO daoRuleset;

    public GetAllUnitConstraintsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<ArmyBuilderUnitConstraint>> execute() {
        final FileHandler<Map<String, Collection<ArmyBuilderUnitConstraint>>> fileUnitConstraints;
        final XMLDocumentReader<Map<String, Collection<ArmyBuilderUnitConstraint>>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitConstraintsXMLDocumentReader(getRulesetDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_FACTION_UNITS,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_FACTION_UNITS));

        fileUnitConstraints = new DefaultXMLFileHandler<>(reader, validator);

        return fileUnitConstraints.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION_UNITS));
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoRuleset = dao;
    }

    protected final RulesetDAO getRulesetDAO() {
        return daoRuleset;
    }

}
