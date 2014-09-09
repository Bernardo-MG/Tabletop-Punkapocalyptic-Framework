package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.ruleset.SpecialRulesXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>>, RulesetDAOAware {

    private RulesetDAO daoSpecialRule;

    public GetAllSpecialRulesCommand() {
        super();
    }

    @Override
    public final Map<String, SpecialRule> execute() {
        final FileHandler<Map<String, SpecialRule>> fileRules;
        final Map<String, SpecialRule> rules;

        fileRules = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, SpecialRule>>(),
                new SpecialRulesXMLDocumentReader(getSpecialRuleDAO()),
                new XSDValidator(
                        ModelFile.VALIDATION_SPECIAL_RULE,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_SPECIAL_RULE)));

        rules = fileRules.read(ResourceUtils
                .getClassPathInputStream(ModelFile.SPECIAL_RULE));

        return rules;
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoSpecialRule = dao;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoSpecialRule;
    }

}
