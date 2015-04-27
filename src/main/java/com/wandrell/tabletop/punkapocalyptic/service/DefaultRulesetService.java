package com.wandrell.tabletop.punkapocalyptic.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.Equipment;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.MeleeWeapon;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.GroupedUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.MutantUnit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.mutation.Mutation;
import com.wandrell.tabletop.punkapocalyptic.repository.WeaponRepository;

public final class DefaultRulesetService implements RulesetService {

    private Integer                   bulletCost;
    private Integer                   packMax;
    private final Map<Object, Object> rulesConfig;
    private final WeaponRepository    weaponRepo;

    public DefaultRulesetService(final Map<Object, Object> config,
            final WeaponRepository weaponRepo) {
        super();

        checkNotNull(config, "Received a null pointer as config map");
        checkNotNull(weaponRepo,
                "Received a null pointer as weapons repository");

        rulesConfig = config;
        this.weaponRepo = weaponRepo;
    }

    @Override
    public final Integer getBulletCost() {
        if (bulletCost == null) {
            bulletCost = Integer.parseInt(getRulesConfiguration().get(
                    "bullet_cost").toString());
        }

        return bulletCost;
    }

    @Override
    public final Integer getGangValoration(final Gang gang) {
        Integer cost;

        cost = 0;
        for (final Unit unit : gang.getUnits()) {
            cost += unit.getValoration();
        }

        cost += (gang.getBullets() * getBulletCost());

        return cost;
    }

    @Override
    public final Integer getMaxAllowedUnits(final Gang gang) {
        final Integer step;
        final Integer range;
        Integer max;
        Integer value;

        // TODO: Maybe this should be loaded from a config file
        step = 3;
        range = 100;

        value = gang.getValoration();
        if (value == 0) {
            max = step;
        } else {
            max = 0;
            while (value > 0) {
                if (value > range) {
                    value -= range;
                } else {
                    value = 0;
                }
                max += step;
            }
        }

        return max;
    }

    @Override
    public final Integer getPackMaxSize() {
        if (packMax == null) {
            packMax = Integer.parseInt(getRulesConfiguration().get(
                    "pack_max_size").toString());
        }

        return packMax;
    }

    @Override
    public final MeleeWeapon getTwoHandedMeleeEquivalent() {
        return getWeaponRepository().getRangedMeleeWeapon();
    }

    @Override
    public final Integer getUnitValoration(final Unit unit) {
        Integer valoration;

        valoration = unit.getUnitTemplate().getBaseCost();

        for (final Weapon weapon : unit.getWeapons()) {
            valoration += weapon.getCost();
        }

        for (final Equipment equipment : unit.getEquipment()) {
            valoration += equipment.getCost();
        }

        if (unit.getArmor() != null) {
            valoration += unit.getArmor().getCost();
        }

        if (unit instanceof MutantUnit) {
            for (final Mutation mutation : ((MutantUnit) unit).getMutations()) {
                valoration += mutation.getCost();
            }
        }

        if (unit instanceof GroupedUnit) {
            valoration = valoration
                    * ((GroupedUnit) unit).getGroupSize().getValue();
        }

        return valoration;
    }

    private final Map<Object, Object> getRulesConfiguration() {
        return rulesConfig;
    }

    private final WeaponRepository getWeaponRepository() {
        return weaponRepo;
    }

}
