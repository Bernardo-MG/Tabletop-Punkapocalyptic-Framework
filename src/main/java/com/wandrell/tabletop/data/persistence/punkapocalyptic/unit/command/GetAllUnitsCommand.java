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
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLValidator;
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
        final XMLAdapter<Map<String, Unit>> adapter;
        final XMLInterpreter<Map<String, Unit>> reader;
        final XMLValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new UnitsXMLDocumentReader(getRulesetService());
        validator = new XSDValidator(ModelFileConf.VALIDATION_UNIT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT));

        fileUnits = new DefaultXMLFileParser<>(adapter, reader, validator);

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

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

}
