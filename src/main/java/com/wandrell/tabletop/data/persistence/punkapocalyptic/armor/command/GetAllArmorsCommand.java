package com.wandrell.tabletop.data.persistence.punkapocalyptic.armor.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.ArmorsParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllArmorsCommand implements
        ReturnCommand<Map<String, Armor>> {

    private final Map<String, ArmorInitializerModifier> modifiers;
    private final Map<String, SpecialRule>              rules;

    public GetAllArmorsCommand(final Map<String, SpecialRule> rules,
            final Map<String, ArmorInitializerModifier> modifiers) {
        super();

        checkNotNull(rules, "Received a null pointer as rules");
        checkNotNull(modifiers, "Received a null pointer as modifiers");

        this.rules = rules;
        this.modifiers = modifiers;
    }

    @Override
    public final Map<String, Armor> execute() throws Exception {
        final ObjectParser<Map<String, Armor>> fileArmors;
        final ParserInterpreter<Map<String, Armor>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new ArmorsParserInterpreter(getRules(), getModifiers());
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_ARMOR,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_ARMOR));

        fileArmors = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

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
