package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Map;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.FirearmWeaponEnhancement;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.WeaponEnhancement;

public final class TransactionEnhancementParser implements
        Parser<Map<String, Object>, WeaponEnhancement> {

    public TransactionEnhancementParser() {
        super();
    }

    @Override
    public final WeaponEnhancement parse(final Map<String, Object> input) {
        final WeaponEnhancement enhancement;

        // TODO: Use a service
        if ("bayonet".equals(input.get("name"))) {
            enhancement = new FirearmWeaponEnhancement("bayonet",
                    (Integer) input.get("cost"));
        } else {
            enhancement = null;
        }

        return enhancement;
    }

}
