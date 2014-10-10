package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitsParserInterpreter;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllUnitsCommand implements
        ReturnCommand<Map<String, Unit>>, RulesetServiceAware {

    private RulesetService serviceRuleset;

    public GetAllUnitsCommand() {
        super();
    }

    @Override
    public final Map<String, Unit> execute() throws Exception {
        final ObjectParser<Map<String, Unit>> fileUnits;
        final ParserInterpreter<Map<String, Unit>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new UnitsParserInterpreter(getRulesetService());
        validator = new JDOMXSDValidator(ModelFileConf.VALIDATION_UNIT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT));

        fileUnits = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

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
