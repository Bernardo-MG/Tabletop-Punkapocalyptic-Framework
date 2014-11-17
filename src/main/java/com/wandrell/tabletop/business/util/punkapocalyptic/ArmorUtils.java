package com.wandrell.tabletop.business.util.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.RangedArmor;

public final class ArmorUtils {

    public static final String getArmor(final Armor armor) {
        final String result;
        final Integer arm;
        final Integer arms;
        final Integer armm;
        final Integer arml;

        checkNotNull(armor, "Received a null pointer as armor");

        if (armor instanceof RangedArmor) {
            arm = armor.getArmor();
            arms = ((RangedArmor) armor).getRangedArmor().getShortValue();
            armm = ((RangedArmor) armor).getRangedArmor().getMediumValue();
            arml = ((RangedArmor) armor).getRangedArmor().getLongValue();

            result = String.format("%d (%d/%d/%d)", arm, arms, armm, arml);
        } else {
            result = armor.getArmor().toString();
        }

        return result;
    }

    private ArmorUtils() {
        super();
    }

}
