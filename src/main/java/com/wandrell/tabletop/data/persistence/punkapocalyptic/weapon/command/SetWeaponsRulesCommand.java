package com.wandrell.tabletop.data.persistence.punkapocalyptic.weapon.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.AbstractWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.MeleeWeaponsRulesXMLDocumentReader;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.RangedWeaponsRulesXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.Command;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class SetWeaponsRulesCommand implements Command {

    private final Map<String, SpecialRule> rules;
    private final Map<String, Weapon>      weapons;

    public SetWeaponsRulesCommand(final Map<String, Weapon> weapons,
            final Map<String, SpecialRule> rules) {
        super();

        this.weapons = weapons;
        this.rules = rules;
    }

    @Override
    public final void execute() throws Exception {
        final ObjectParser<Map<String, Collection<SpecialRule>>> fileRulesMelee;
        final JDOMXMLInterpreter<Map<String, Collection<SpecialRule>>> readerMelee;
        final JDOMXMLValidator validatorMelee;
        final ObjectParser<Map<String, Collection<SpecialRule>>> fileRulesRanged;
        final JDOMXMLInterpreter<Map<String, Collection<SpecialRule>>> readerRanged;
        final JDOMXMLValidator validatorRanged;
        final Map<String, Collection<SpecialRule>> rules;

        rules = new LinkedHashMap<>();

        readerMelee = new MeleeWeaponsRulesXMLDocumentReader(getRules());
        validatorMelee = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileRulesMelee = ParserUtils.getJDOMXMLParser(readerMelee,
                validatorMelee);

        rules.putAll(fileRulesMelee.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE)));

        readerRanged = new RangedWeaponsRulesXMLDocumentReader(getRules());
        validatorRanged = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRulesRanged = ParserUtils.getJDOMXMLParser(readerRanged,
                validatorRanged);

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
