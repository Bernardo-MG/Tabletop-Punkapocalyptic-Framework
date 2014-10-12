package com.wandrell.tabletop.business.util.file.punkapocalyptic.unit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelNodeConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;

public final class UnitAvailableWeaponsParserInterpreter implements
        ParserInterpreter<Map<String, Collection<Weapon>>, Document> {

    private final Map<String, Weapon> weapons;

    public UnitAvailableWeaponsParserInterpreter(
            final Map<String, Weapon> weapons) {
        super();

        checkNotNull(weapons, "Received a null pointer as weapons");

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Collection<Weapon>> parse(final Document doc) {
        final Element root;
        final Map<String, Collection<Weapon>> weapons;
        Collection<Weapon> weaponList;

        checkNotNull(doc, "Received a null pointer as doc");

        root = doc.getRootElement();

        weapons = new LinkedHashMap<>();
        for (final Element node : root.getChildren()) {
            weaponList = getWeapons(node.getChild(ModelNodeConf.WEAPONS));

            weapons.put(node.getChildText(ModelNodeConf.UNIT), weaponList);
        }

        return weapons;
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

    private final Collection<Weapon> getWeapons(final Element weaponsNode) {
        final Collection<Weapon> weaponList;

        weaponList = new LinkedList<>();
        if (weaponsNode != null) {
            for (final Element weapon : weaponsNode.getChildren()) {
                weaponList.add(getWeapons().get(weapon.getText()));
            }
        }

        return weaponList;
    }

}
