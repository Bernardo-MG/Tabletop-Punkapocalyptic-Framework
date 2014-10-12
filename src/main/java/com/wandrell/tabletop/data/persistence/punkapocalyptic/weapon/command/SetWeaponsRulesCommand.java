package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.AbstractWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsRulesParserInterpreter;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.RangedWeaponsRulesParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.Command;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class SetWeaponsRulesCommand implements Command {

    private final Map<String, SpecialRule> rules;
    private final Map<String, Weapon>      weapons;

    public SetWeaponsRulesCommand(final Map<String, Weapon> weapons,
            final Map<String, SpecialRule> rules) {
        super();

        checkNotNull(weapons, "Received a null pointer as weapons");
        checkNotNull(rules, "Received a null pointer as rules");

        this.weapons = weapons;
        this.rules = rules;
    }

    @Override
    public final void execute() throws Exception {
        final ObjectParser<Map<String, Collection<SpecialRule>>> fileRulesMelee;
        final ParserInterpreter<Map<String, Collection<SpecialRule>>, Document> readerMelee;
        final ParserValidator<Document, SAXBuilder> validatorMelee;
        final ObjectParser<Map<String, Collection<SpecialRule>>> fileRulesRanged;
        final ParserInterpreter<Map<String, Collection<SpecialRule>>, Document> readerRanged;
        final ParserValidator<Document, SAXBuilder> validatorRanged;
        final Map<String, Collection<SpecialRule>> rules;

        rules = new LinkedHashMap<>();

        readerMelee = new MeleeWeaponsRulesParserInterpreter(getRules());
        validatorMelee = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileRulesMelee = ParserFactory.getInstance().getJDOMXMLParser(
                readerMelee, validatorMelee);

        rules.putAll(fileRulesMelee.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE)));

        readerRanged = new RangedWeaponsRulesParserInterpreter(getRules());
        validatorRanged = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRulesRanged = ParserFactory.getInstance().getJDOMXMLParser(
                readerRanged, validatorRanged);

        rules.putAll(fileRulesRanged.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_RANGED)));

        for (final Entry<String, Collection<SpecialRule>> entry : rules
                .entrySet()) {
            ((AbstractWeapon) getWeapons().get(entry.getKey())).setRules(entry
                    .getValue());
        }
    }

    private final Map<String, SpecialRule> getRules() {
        return rules;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
