package com.wandrell.tabletop.util.command.punkapocalyptic.dao.ruleset;

import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.data.dao.punkapocalyptic.RulesetDAO;
import com.wandrell.tabletop.model.punkapocalyptic.ruleset.SpecialRule;
import com.wandrell.tabletop.util.file.punkapocalyptic.ruleset.SpecialRulesXMLDocumentReader;
import com.wandrell.tabletop.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

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
