package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import java.util.Properties;

import com.wandrell.util.command.ReturnCommand;

public final class GetBulletCostFromPropertiesCommand implements
        ReturnCommand<Integer> {

    private static final String KEY = "bullet_cost";
    private final Properties    rulesConfig;

    public GetBulletCostFromPropertiesCommand(final Properties rulesConfig) {
        super();

        this.rulesConfig = rulesConfig;
    }

    @Override
    public final Integer execute() throws Exception {
        return Integer.parseInt(getRulesConfiguration().getProperty(KEY));
    }

    private final Properties getRulesConfiguration() {
        return rulesConfig;
    }

}
