package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.ruleset.SpecialRulesXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>>, RulesetDAOAware {

    private RulesetDAO daoSpecialRule;

    public GetAllSpecialRulesCommand() {
        super();
    }

    @Override
    public final Map<String, SpecialRule> execute() {
        final FileHandler<Map<String, SpecialRule>> fileRules;
        final XMLDocumentReader<Map<String, SpecialRule>> reader;
        final XMLDocumentValidator validator;

        reader = new SpecialRulesXMLDocumentReader(getSpecialRuleDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_SPECIAL_RULE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_SPECIAL_RULE));

        fileRules = new DefaultXMLFileHandler<>(reader, validator);

        return fileRules.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.SPECIAL_RULE));
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoSpecialRule = dao;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoSpecialRule;
    }

}
