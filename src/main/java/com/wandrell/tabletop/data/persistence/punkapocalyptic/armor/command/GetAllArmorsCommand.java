package com.wandrell.tabletop.data.persistence.punkapocalyptic.armor.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.ArmorsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllArmorsCommand implements
        ReturnCommand<Map<String, Armor>> {

    private final Map<String, ArmorInitializerModifier> modifiers;
    private final Map<String, SpecialRule>              rules;

    public GetAllArmorsCommand(final Map<String, SpecialRule> rules,
            final Map<String, ArmorInitializerModifier> modifiers) {
        super();

        this.rules = rules;
        this.modifiers = modifiers;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final ObjectParser<Map<String, Armor>> fileArmors;
        final JDOMXMLInterpreter<Map<String, Armor>> reader;
        final JDOMXMLValidator validator;

        reader = new ArmorsXMLDocumentReader(getRules(), getModifiers());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_ARMOR,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_ARMOR));

        fileArmors = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.ARMOR));
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

    protected final Map<String, ArmorInitializerModifier> getModifiers() {
        return modifiers;
    }

}
