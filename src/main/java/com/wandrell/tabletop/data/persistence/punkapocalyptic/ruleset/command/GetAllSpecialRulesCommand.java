package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.ruleset.SpecialRulesXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>> {

    private final Map<String, Weapon> weapons;

    public GetAllSpecialRulesCommand(final Map<String, Weapon> weapons) {
        super();

        this.weapons = weapons;
    }

    @Override
    public final Map<String, SpecialRule> execute() throws Exception {
        final FileParser<Map<String, SpecialRule>> fileRules;
        final XMLDocumentReader<Map<String, SpecialRule>> reader;
        final XMLDocumentValidator validator;

        reader = new SpecialRulesXMLDocumentReader((MeleeWeapon) getWeapons()
                .get(WeaponNameConf.IMPROVISED_WEAPON));
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_SPECIAL_RULE,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_SPECIAL_RULE));

        fileRules = new DefaultXMLFileParser<>(reader, validator);

        return fileRules.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.SPECIAL_RULE));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
