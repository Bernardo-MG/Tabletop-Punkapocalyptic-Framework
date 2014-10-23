/**
 * Copyright 2014 the original author or authors
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
package com.wandrell.tabletop.business.model.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.specialrule.SpecialRule;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitListener;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;

public final class AvailabilityUnitWrapper implements AvailabilityUnit {

    private final Collection<Armor>             armorOptions       = new LinkedHashSet<>();
    private final Collection<Equipment>         equipment          = new LinkedHashSet<>();
    private final Unit                          unit;
    private final Collection<WeaponEnhancement> weaponEnhancements = new LinkedHashSet<>();

    public AvailabilityUnitWrapper(final AvailabilityUnitWrapper unit) {
        super();

        checkNotNull(unit, "Received a null pointer as the unit");

        this.unit = unit.unit.createNewInstance();

        armorOptions.addAll(unit.armorOptions);
    }

    public AvailabilityUnitWrapper(final Unit unit,
            final Collection<Armor> armorOptions,
            final Collection<WeaponEnhancement> weaponEnhancements,
            final Collection<Equipment> equipment) {
        super();

        checkNotNull(unit, "Received a null pointer as the unit");
        checkNotNull(armorOptions, "Received a null pointer as armor options");

        this.unit = unit;

        for (final Armor armor : armorOptions) {
            checkNotNull(armor, "Received a null pointer as armor");

            this.armorOptions.add(armor);
        }

        for (final WeaponEnhancement enhancement : weaponEnhancements) {
            checkNotNull(enhancement,
                    "Received a null pointer as weapon enhancement");

            this.weaponEnhancements.add(enhancement);
        }

        for (final Equipment equip : equipment) {
            checkNotNull(equip,
                    "Received a null pointer as equipment piece option");

            this.equipment.add(equip);
        }
    }

    @Override
    public final void addEquipment(final Equipment equipment) {
        checkNotNull(equipment, "Received a null pointer as equipment");

        getUnit().addEquipment(equipment);
    }

    @Override
    public final void addUnitListener(final UnitListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getUnit().addUnitListener(listener);
    }

    @Override
    public final void addWeapon(final Weapon weapon) {
        checkNotNull(weapon, "Received a null pointer as weapon");

        getUnit().addWeapon(weapon);
    }

    @Override
    public final void clearEquipment() {
        getUnit().clearEquipment();
    }

    @Override
    public final void clearWeapons() {
        getUnit().clearWeapons();
    }

    @Override
    public final AvailabilityUnitWrapper createNewInstance() {
        return new AvailabilityUnitWrapper(this);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AvailabilityUnitWrapper other = (AvailabilityUnitWrapper) obj;

        return Objects.equals(unit, other.unit);
    }

    @Override
    public final Integer getActions() {
        return getUnit().getActions();
    }

    @Override
    public final Integer getAgility() {
        return getUnit().getAgility();
    }

    @Override
    public final Armor getArmor() {
        return getUnit().getArmor();
    }

    @Override
    public final Collection<Armor> getArmorOptions() {
        return Collections.unmodifiableCollection(getArmorOptionsModifiable());
    }

    @Override
    public final Integer getBaseCost() {
        return getUnit().getBaseCost();
    }

    @Override
    public final Integer getCombat() {
        return getUnit().getCombat();
    }

    @Override
    public final Collection<Equipment> getEquipment() {
        return getUnit().getEquipment();
    }

    @Override
    public final Collection<Equipment> getEquipmentOptions() {
        return Collections
                .unmodifiableCollection(getEquipmentOptionsModifiable());
    }

    @Override
    public final Integer getPrecision() {
        return getUnit().getPrecision();
    }

    @Override
    public final Collection<SpecialRule> getSpecialRules() {
        return getUnit().getSpecialRules();
    }

    @Override
    public final Integer getStrength() {
        return getUnit().getStrength();
    }

    @Override
    public final Integer getTech() {
        return getUnit().getTech();
    }

    @Override
    public final Integer getToughness() {
        return getUnit().getToughness();
    }

    @Override
    public final String getUnitName() {
        return getUnit().getUnitName();
    }

    @Override
    public final ValueHandler getValoration() {
        return getUnit().getValoration();
    }

    @Override
    public final Collection<WeaponEnhancement> getWeaponEnhancementOptions() {
        return Collections
                .unmodifiableCollection(getWeaponEnhancementsModifiable());
    }

    @Override
    public final Collection<Weapon> getWeapons() {
        return getUnit().getWeapons();
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(unit);
    }

    @Override
    public final void removeEquipment(final Equipment equipment) {
        getUnit().removeEquipment(equipment);
    }

    @Override
    public final void removeUnitListener(final UnitListener listener) {
        getUnit().removeUnitListener(listener);
    }

    @Override
    public final void removeWeapon(final Weapon weapon) {
        getUnit().removeWeapon(weapon);
    }

    @Override
    public final void setArmor(final Armor armor) {
        checkNotNull(armor, "Received a null pointer as armor");

        getUnit().setArmor(armor);
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("name", getUnitName())
                .toString();
    }

    private final Collection<Armor> getArmorOptionsModifiable() {
        return armorOptions;
    }

    private final Collection<Equipment> getEquipmentOptionsModifiable() {
        return equipment;
    }

    private final Unit getUnit() {
        return unit;
    }

    private final Collection<WeaponEnhancement>
            getWeaponEnhancementsModifiable() {
        return weaponEnhancements;
    }

}
