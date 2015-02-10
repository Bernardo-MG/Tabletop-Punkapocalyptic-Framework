package com.wandrell.tabletop.business.service.punkapocalyptic.ruleset.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.pattern.command.ReturnCommand;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;

public final class GetGangValorationCommand implements ReturnCommand<Integer>,
        RulesetServiceAware {

    private final Gang     gang;
    private RulesetService serviceRuleset;

    public GetGangValorationCommand(final Gang gang) {
        super();

        checkNotNull(gang, "Received a null pointer as gang");

        this.gang = gang;
    }

    @Override
    public final Integer execute() {
        Integer cost;

        cost = 0;
        for (final Unit unit : getGang().getUnits()) {
            cost += unit.getValoration();
        }

        cost += (getGang().getBullets() * getRulesetService().getBulletCost());

        return cost;
    }

    public final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    @Override
    public final void setRulesetService(final RulesetService service) {
        checkNotNull(service, "Received a null pointer as ruleset service");

        serviceRuleset = service;
    }

    private final Gang getGang() {
        return gang;
    }

}
