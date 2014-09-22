package com.wandrell.tabletop.business.util;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedWeapon.RangedDistance;

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

        if ((weapon.getShortStrength().equals(weapon.getMediumStrength()))
                && (weapon.getShortStrength().equals(weapon.getLongStrength()))) {
            strength = weapon.getShortStrength().toString();
        } else {
            strength = String.format("%d/%d/%d", weapon.getShortStrength(),
                    weapon.getMediumStrength(), weapon.getLongStrength());
        }

        return strength;
    }

    private static final String getDistanceRanges(final RangedDistance ranges) {
        final String distance;

        if ((ranges.getShortDistance().equals(ranges.getMediumDistance()))
                && (ranges.getShortDistance().equals(ranges.getLongDistance()))) {
            distance = ranges.getShortDistance().toString();
        } else {
            distance = String.format("%d/%d/%d", ranges.getShortDistance(),
                    ranges.getMediumDistance(), ranges.getLongDistance());
        }

        return distance;
    }

    private WeaponUtils() {
        super();
    }

}
