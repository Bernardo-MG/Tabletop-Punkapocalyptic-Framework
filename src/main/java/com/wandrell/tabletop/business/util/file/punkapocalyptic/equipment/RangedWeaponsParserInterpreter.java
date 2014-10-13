package com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.DefaultRangedValue;
import com.wandrell.tabletop.business.model.punkapocalyptic.RangedValue;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.DefaultRangedWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.MeleeWeapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class RangedWeaponsParserInterpreter implements
        ParserInterpreter<Map<String, Weapon>, Document> {

    private final MeleeWeapon melee;

    public RangedWeaponsParserInterpreter(final MeleeWeapon defaultWeapon) {
        super();

        checkNotNull(defaultWeapon, "Received a null pointer as default weapon");

        melee = defaultWeapon;
    }

    @Override
    public final Map<String, Weapon> parse(final Document doc) {
        final Element root;
        final Map<String, Weapon> weapons;
        String name;
        Element strength;
        Element penetration;
        Element range;
        Element distance;
        Integer strengthShort;
        Integer strengthMedium;
        Integer strengthLong;
        Integer penetrationShort;
        Integer penetrationMedium;
        Integer penetrationLong;
        Integer distanceShortCM;
        Integer distanceMediumCM;
        Integer distanceLongCM;
        Integer distanceShortInches;
        Integer distanceMediumInches;
        Integer distanceLongInches;
        Integer cost;
        RangedValue distanceCM;
        RangedValue distanceInches;
        RangedValue strengthRanged;
        RangedValue penetrationRanged;
        Weapon weapon;

        checkNotNull(doc, "Received a null pointer as document");

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            name = node.getChildText(ModelNodeConf.NAME);

            strength = node.getChild(ModelNodeConf.STRENGTH);
            strengthShort = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.SHORT));
            strengthMedium = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.MEDIUM));
            strengthLong = Integer.parseInt(strength
                    .getChildText(ModelNodeConf.LONG));

            penetration = node.getChild(ModelNodeConf.PENETRATION);
            penetrationShort = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.SHORT));
            penetrationMedium = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.MEDIUM));
            penetrationLong = Integer.parseInt(penetration
                    .getChildText(ModelNodeConf.LONG));

            range = node.getChild(ModelNodeConf.RANGE);

            distance = range.getChild(ModelNodeConf.INCHES);
            distanceShortInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.SHORT));
            distanceMediumInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.MEDIUM));
            distanceLongInches = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.LONG));

            distance = range.getChild(ModelNodeConf.CM);
            distanceShortCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.SHORT));
            distanceMediumCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.MEDIUM));
            distanceLongCM = Integer.parseInt(distance
                    .getChildText(ModelNodeConf.LONG));

            cost = Integer.parseInt(node.getChildText(ModelNodeConf.COST));

            distanceCM = new DefaultRangedValue(distanceShortCM,
                    distanceMediumCM, distanceLongCM);
            distanceInches = new DefaultRangedValue(distanceShortInches,
                    distanceMediumInches, distanceLongInches);

            penetrationRanged = new DefaultRangedValue(penetrationShort,
                    penetrationMedium, penetrationLong);
            strengthRanged = new DefaultRangedValue(strengthShort,
                    strengthMedium, strengthLong);

            weapon = new DefaultRangedWeapon(name, cost, penetrationRanged,
                    strengthRanged, distanceCM, distanceInches,
                    getDefaultMeleeWeapon());

            weapons.put(name, weapon);
        }

        return weapons;
    }

    private final MeleeWeapon getDefaultMeleeWeapon() {
        return melee;
    }

}
