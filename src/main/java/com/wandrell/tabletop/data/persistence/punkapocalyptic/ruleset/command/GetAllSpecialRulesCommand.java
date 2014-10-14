package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.SpecialRuleNameConf;
import com.wandrell.tabletop.business.conf.WeaponNameConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.DefaultSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.FirearmSpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.TwoHandedSpecialRule;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllSpecialRulesCommand implements
        ReturnCommand<Map<String, SpecialRule>> {

    private final Map<String, Weapon> weapons;

    public GetAllSpecialRulesCommand(final Map<String, Weapon> weapons) {
        super();

        checkNotNull(weapons, "Received a null pointer as weapons");

        this.weapons = weapons;
    }

    @Override
    public final Map<String, SpecialRule> execute() throws Exception {
        final Map<String, SpecialRule> rules;

        rules = new LinkedHashMap<>();

        // TODO : Use a Spring context or something
        rules.put(
                SpecialRuleNameConf.TWO_HANDED,
                new TwoHandedSpecialRule(SpecialRuleNameConf.TWO_HANDED,
                        (MeleeWeapon) getWeapons().get(
                                WeaponNameConf.IMPROVISED_WEAPON)));

        rules.put("automatic", new DefaultSpecialRule("automatic"));
        rules.put("cumbersome", new DefaultSpecialRule("cumbersome"));
        rules.put("dead_slow", new DefaultSpecialRule("dead_slow"));
        rules.put("firearm", new FirearmSpecialRule("firearm"));
        rules.put("hard_to_use", new DefaultSpecialRule("hard_to_use"));
        rules.put("pellets", new DefaultSpecialRule("pellets"));

        return rules;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
