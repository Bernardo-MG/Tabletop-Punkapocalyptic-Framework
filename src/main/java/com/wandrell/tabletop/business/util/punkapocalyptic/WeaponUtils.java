package com.wandrell.tabletop.business.util.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.util.RangedValue;

public final class WeaponUtils {

    public static final String getRangedWeaponDistanceImperial(
            final RangedWeapon weapon) {
        return getDistanceRanges(weapon.getDistancesImperialUnits());
    }

    public static final String getRangedWeaponDistanceMetric(
            final RangedWeapon weapon) {
        return getDistanceRanges(weapon.getDistancesMetricSystem());
    }

    public static final String getRangedWeaponPenetration(
            final RangedWeapon weapon) {
        final String penetration;

        checkNotNull(weapon, "Received a null pointer as weapon");

        if ((weapon.getShortPenetration().equals(weapon.getMediumPenetration()))
                && (weapon.getShortPenetration().equals(weapon
                        .getLongPenetration()))) {
            penetration = weapon.getShortPenetration().toString();
        } else {
            penetration = String.format("%d/%d/%d",
                    weapon.getShortPenetration(),
                    weapon.getMediumPenetration(), weapon.getLongPenetration());
        }

        return penetration;
    }

    public static final String
            getRangedWeaponStrength(final RangedWeapon weapon) {
        final String strength;

        checkNotNull(weapon, "Received a null pointer as weapon");

        if ((weapon.getShortStrength().equals(weapon.getMediumStrength()))
                && (weapon.getShortStrength().equals(weapon.getLongStrength()))) {
            strength = weapon.getShortStrength().toString();
        } else {
            strength = String.format("%d/%d/%d", weapon.getShortStrength(),
                    weapon.getMediumStrength(), weapon.getLongStrength());
        }

        return strength;
    }

    private static final String getDistanceRanges(final RangedValue ranges) {
        final String distance;

        checkNotNull(ranges, "Received a null pointer as ranges");

        if ((ranges.getShortValue().equals(ranges.getMediumValue()))
                && (ranges.getShortValue().equals(ranges.getLongValue()))) {
            distance = ranges.getShortValue().toString();
        } else {
            distance = String.format("%d/%d/%d", ranges.getShortValue(),
                    ranges.getMediumValue(), ranges.getLongValue());
        }

        return distance;
    }

    private WeaponUtils() {
        super();
    }

}
