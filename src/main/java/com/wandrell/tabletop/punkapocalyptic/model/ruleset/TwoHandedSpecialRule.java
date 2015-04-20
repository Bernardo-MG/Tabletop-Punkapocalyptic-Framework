/**
 * Copyright 2015 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.wandrell.tabletop.punkapocalyptic.model.ruleset;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.RangedWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class TwoHandedSpecialRule implements SpecialRule,
        WeaponModifierSpecialRule {

    private final SpecialRule    rule;
    private final RulesetService rulesetService;

    public TwoHandedSpecialRule(final String name, final RulesetService service) {
        super();

        checkNotNull(name, "Received a null pointer as name");
        checkNotNull(service, "Received a null pointer as ruleset service");

        rule = new DefaultSpecialRule(name);
        rulesetService = service;
    }

    public TwoHandedSpecialRule(final TwoHandedSpecialRule rule) {
        super();

        checkNotNull(rule, "Received a null pointer as rule");

        this.rule = rule.rule.createNewInstance();
        rulesetService = rule.rulesetService;
    }

    @Override
    public final void applyToWeapon(final Weapon weapon) {
        checkNotNull(weapon, "Received a null pointer as weapon");

        weapon.setTwoHanded(true);

        if (weapon instanceof RangedWeapon) {
            ((RangedWeapon) weapon).setMeleeEquivalent(getRulesetService()
                    .getTwoHandedMeleeEquivalent());
        }
    }

    @Override
    public final TwoHandedSpecialRule createNewInstance() {
        return new TwoHandedSpecialRule(this);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final TwoHandedSpecialRule other;

        other = (TwoHandedSpecialRule) obj;
        return Objects.equals(rule, other.rule);
    }

    @Override
    public final String getName() {
        return getBaseRule().getName();
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(rule);
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

    private final SpecialRule getBaseRule() {
        return rule;
    }

    private final RulesetService getRulesetService() {
        return rulesetService;
    }

}
