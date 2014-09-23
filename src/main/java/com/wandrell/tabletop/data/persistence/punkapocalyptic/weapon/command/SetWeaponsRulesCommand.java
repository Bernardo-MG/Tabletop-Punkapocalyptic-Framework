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
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

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
        final FileParser<Map<String, Collection<SpecialRule>>> fileRulesMelee;
        final XMLAdapter<Map<String, Collection<SpecialRule>>> adapter;
        final XMLInterpreter<Map<String, Collection<SpecialRule>>> readerMelee;
        final XMLValidator validatorMelee;
        final FileParser<Map<String, Collection<SpecialRule>>> fileRulesRanged;
        final XMLInterpreter<Map<String, Collection<SpecialRule>>> readerRanged;
        final XMLValidator validatorRanged;
        final Map<String, Collection<SpecialRule>> rules;

        rules = new LinkedHashMap<>();

        adapter = new JDOMAdapter<>();
        readerMelee = new MeleeWeaponsRulesXMLDocumentReader(getRules());
        validatorMelee = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_MELEE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_MELEE));

        fileRulesMelee = new DefaultXMLFileParser<>(adapter, readerMelee,
                validatorMelee);

        rules.putAll(fileRulesMelee.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_MELEE)));

        readerRanged = new RangedWeaponsRulesXMLDocumentReader(getRules());
        validatorRanged = new XSDValidator(
                ModelFileConf.VALIDATION_WEAPON_RANGED,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_RANGED));

        fileRulesRanged = new DefaultXMLFileParser<>(adapter, readerRanged,
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
