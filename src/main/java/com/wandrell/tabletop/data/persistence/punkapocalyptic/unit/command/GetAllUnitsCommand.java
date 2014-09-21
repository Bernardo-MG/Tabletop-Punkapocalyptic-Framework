package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitsCommand implements
        ReturnCommand<Map<String, Unit>>, RulesetServiceAware {

    private RulesetService serviceRuleset;

    public GetAllUnitsCommand() {
        super();
    }

    @Override
    public final Map<String, Unit> execute() throws Exception {
        final FileParser<Map<String, Unit>> fileUnits;
        final XMLDocumentReader<Map<String, Unit>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitsXMLDocumentReader(getRulesetService());
        validator = new XSDValidator(ModelFileConf.VALIDATION_UNIT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT));

        fileUnits = new DefaultXMLFileParser<>(reader, validator);

        return fileUnits.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT));
    }

    @Override
    public final void setRulesetService(final RulesetService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as ruleset service");
        }

        serviceRuleset = service;
    }

    protected final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
