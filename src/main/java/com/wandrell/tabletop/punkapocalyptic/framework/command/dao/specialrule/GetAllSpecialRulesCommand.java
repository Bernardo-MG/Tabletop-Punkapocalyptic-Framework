package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.specialrule;

import java.util.Map;

import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.SpecialRulesXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.rule.SpecialRule;
import com.wandrell.util.PathUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>> {

    public GetAllSpecialRulesCommand() {
        super();
    }

    @Override
    public final Map<String, SpecialRule> execute() {
        final FileHandler<Map<String, SpecialRule>> fileRules;
        final Map<String, SpecialRule> rules;

        fileRules = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, SpecialRule>>(),
                new SpecialRulesXMLDocumentReader(),
                new XSDValidator(
                        PathUtils
                                .getClassPathResource(ModelFile.VALIDATION_SPECIAL_RULE)));

        rules = fileRules.read(PathUtils
                .getClassPathResource(ModelFile.SPECIAL_RULE));

        return rules;
    }

}
