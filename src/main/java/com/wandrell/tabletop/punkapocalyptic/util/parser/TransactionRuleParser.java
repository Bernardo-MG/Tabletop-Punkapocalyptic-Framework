package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Map;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.DefaultSpecialRule;
import com.wandrell.tabletop.punkapocalyptic.model.ruleset.SpecialRule;

public final class TransactionRuleParser implements
        Parser<Map<String, Object>, SpecialRule> {

    public TransactionRuleParser() {
        super();
    }

    @Override
    public final SpecialRule parse(final Map<String, Object> input) {
        // TODO: Use a service
        return new DefaultSpecialRule(input.get("name").toString());
    }

}
