package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.specialrule;

import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.SpecialRuleDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.SpecialRuleDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.SpecialRulesXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>>, SpecialRuleDAOAware {

    private SpecialRuleDAO daoSpecialRule;

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
    public final void setSpecialRuleDAO(final SpecialRuleDAO dao) {
        daoSpecialRule = dao;
    }

    protected final SpecialRuleDAO getSpecialRuleDAO() {
        return daoSpecialRule;
    }

}
