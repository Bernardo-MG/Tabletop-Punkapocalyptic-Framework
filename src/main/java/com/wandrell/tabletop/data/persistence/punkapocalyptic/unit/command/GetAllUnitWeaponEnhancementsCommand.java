package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllUnitWeaponEnhancementsCommand implements
        ReturnCommand<Map<String, Collection<WeaponEnhancement>>> {

    private final Map<String, WeaponEnhancement> enhancement;

    public GetAllUnitWeaponEnhancementsCommand(
            final Map<String, WeaponEnhancement> enhancement) {
        super();

        this.enhancement = enhancement;
    }

    @Override
    public final Map<String, Collection<WeaponEnhancement>> execute()
            throws Exception {
        getEnhancements();
        return new LinkedHashMap<>();
    }

    private final Map<String, WeaponEnhancement> getEnhancements() {
        return enhancement;
    }

}
