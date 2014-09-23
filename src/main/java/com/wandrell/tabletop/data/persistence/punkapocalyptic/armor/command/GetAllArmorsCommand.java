package com.wandrell.tabletop.data.persistence.punkapocalyptic.armor.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.ArmorsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLDocumentInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllArmorsCommand implements
        ReturnCommand<Map<String, Armor>> {

    private final Map<String, SpecialRule> rules;

    public GetAllArmorsCommand(final Map<String, SpecialRule> rules) {
        super();

        this.rules = rules;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final FileParser<Map<String, Armor>> fileArmors;
        final XMLAdapter<Map<String, Armor>> adapter;
        final XMLDocumentInterpreter<Map<String, Armor>> reader;
        final XMLDocumentValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new ArmorsXMLDocumentReader(getRules());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_ARMOR,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_ARMOR));

        fileArmors = new DefaultXMLFileParser<>(adapter, reader, validator);

        return fileArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.ARMOR));
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

}
