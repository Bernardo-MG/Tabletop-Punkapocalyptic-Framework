package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.DefaultMeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;

public final class TransactionMeleeWeaponParser implements
        Parser<Map<String, Object>, Weapon> {

    private final Repository<SpecialRule> rulesRepo;

    public TransactionMeleeWeaponParser(final Repository<SpecialRule> rulesRepo) {
        super();

        this.rulesRepo = rulesRepo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Weapon parse(final Map<String, Object> input) {
        final Collection<SpecialRule> rules;
        final Collection<String> ruleNames;

        ruleNames = (Collection<String>) input.get("rules");

        rules = rulesRepo.getCollection(new Predicate<SpecialRule>() {

            @Override
            public final boolean apply(final SpecialRule input) {
                return ruleNames.contains(input.getName());
            }

        });

        // TODO: Use a service
        return new DefaultMeleeWeapon(input.get("name").toString(),
                (Integer) input.get("cost"), (Integer) input.get("strength"),
                (Integer) input.get("penetration"),
                (Integer) input.get("combat"), rules);
    }

}
