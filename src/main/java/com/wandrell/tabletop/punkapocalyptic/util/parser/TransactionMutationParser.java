package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Map;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.DefaultMutation;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;

public final class TransactionMutationParser implements
        Parser<Map<String, Object>, Mutation> {

    public TransactionMutationParser() {
        super();
    }

    @Override
    public final Mutation parse(final Map<String, Object> input) {
        // TODO: Use a service
        return new DefaultMutation(input.get("name").toString(),
                (Integer) input.get("cost"), (Integer) input.get("actions"),
                (Integer) input.get("agility"), (Integer) input.get("combat"),
                (Integer) input.get("precision"),
                (Integer) input.get("strength"), (Integer) input.get("tech"),
                (Integer) input.get("toughness"));
    }

}
