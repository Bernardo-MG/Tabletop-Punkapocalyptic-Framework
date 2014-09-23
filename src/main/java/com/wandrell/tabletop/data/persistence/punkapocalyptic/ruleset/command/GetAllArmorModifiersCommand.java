package com.wandrell.tabletop.data.persistence.punkapocalyptic.ruleset.command;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModifiersConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.ArmorInitializerModifier;
import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.modifier.BulletproofArmorInitializerModifier;
import com.wandrell.util.command.ReturnCommand;

public final class GetAllArmorModifiersCommand implements
        ReturnCommand<Map<String, ArmorInitializerModifier>> {

    public GetAllArmorModifiersCommand() {
        super();
    }

    @Override
    public final Map<String, ArmorInitializerModifier> execute() {
        final Map<String, ArmorInitializerModifier> modifiers;

        modifiers = new LinkedHashMap<>();
        modifiers.put(ModifiersConf.FIREARMS_PROTECTION,
                new BulletproofArmorInitializerModifier());

        return modifiers;
    }

}
