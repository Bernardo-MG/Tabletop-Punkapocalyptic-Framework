package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitsCommand implements
        ReturnCommand<Collection<Unit>>, RulesetDAOAware {

    private RulesetDAO daoRuleset;

    public GetAllUnitsCommand() {
        super();
    }

    @Override
    public final Collection<Unit> execute() {
        final FileHandler<Collection<Unit>> fileUnits;
        final XMLDocumentReader<Collection<Unit>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitsXMLDocumentReader(getRulesetDAO());
        validator = new XSDValidator(ModelFileConf.VALIDATION_UNIT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT));

        fileUnits = new DefaultXMLFileHandler<>(reader, validator);

        return fileUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT));
    }

    @Override
    public void setRulesetDAO(final RulesetDAO dao) {
        daoRuleset = dao;
    }

    protected final RulesetDAO getRulesetDAO() {
        return daoRuleset;
    }

}
