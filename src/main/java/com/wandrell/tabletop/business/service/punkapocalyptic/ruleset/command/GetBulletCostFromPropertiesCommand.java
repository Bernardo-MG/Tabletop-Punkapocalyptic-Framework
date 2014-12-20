package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import java.util.Properties;

import com.wandrell.util.command.ReturnCommand;

public final class GetBulletCostFromPropertiesCommand implements
        ReturnCommand<Integer> {

    private final String     key;
    private final Properties rulesConfig;

    public GetBulletCostFromPropertiesCommand(final Properties rulesConfig,
            final String key) {
        super();

        this.key = key;
        this.rulesConfig = rulesConfig;
    }

    @Override
    public final Integer execute() throws Exception {
        return Integer.parseInt(getRulesConfiguration().getProperty(getKey()));
    }

    private final String getKey() {
        return key;
    }

    private final Properties getRulesConfiguration() {
        return rulesConfig;
    }

}
