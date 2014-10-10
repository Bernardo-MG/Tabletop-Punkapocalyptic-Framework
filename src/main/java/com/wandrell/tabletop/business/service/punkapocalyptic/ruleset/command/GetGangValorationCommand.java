package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.util.command.ReturnCommand;

public final class GetGangValorationCommand implements ReturnCommand<Integer>,
        RulesetServiceAware {

    private final Gang     gang;
    private RulesetService serviceRuleset;

    public GetGangValorationCommand(final Gang gang) {
        super();

        this.gang = gang;
    }

    @Override
    public final Integer execute() {
        Integer cost;

        cost = 0;
        for (final Unit unit : getGang().getUnits()) {
            cost += unit.getValoration().getValue();
        }

        cost += (getGang().getBullets().getValue() * getRulesetService()
                .getBulletCost());

        return cost;
    }

    public final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    @Override
    public final void setRulesetService(final RulesetService service) {
        serviceRuleset = service;
    }

    private final Gang getGang() {
        return gang;
    }

}
