package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.ruleset;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.repository.WeaponRepository;
import com.wandrell.tabletop.punkapocalyptic.service.DefaultRulesetService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class TestFilterWeaponOptions {

    private RulesetService service;
    private Weapon         weapon1;
    private Weapon         weapon2;
    private Weapon         weapon3;
    private Weapon         weapon4;
    private Weapon         weapon5;
    private Weapon         weapon6;

    public TestFilterWeaponOptions() {
        super();
    }

    @SuppressWarnings("unchecked")
    @BeforeClass
    public final void initializeWeapons() {
        weapon1 = Mockito.mock(Weapon.class);
        Mockito.when(weapon1.getName()).thenReturn("weapon1");
        Mockito.when(weapon1.isTwoHanded()).thenReturn(true);

        weapon2 = Mockito.mock(Weapon.class);
        Mockito.when(weapon2.getName()).thenReturn("weapon2");
        Mockito.when(weapon2.isTwoHanded()).thenReturn(true);

        weapon3 = Mockito.mock(Weapon.class);
        Mockito.when(weapon3.getName()).thenReturn("weapon3");
        Mockito.when(weapon3.isTwoHanded()).thenReturn(false);

        weapon4 = Mockito.mock(Weapon.class);
        Mockito.when(weapon4.getName()).thenReturn("weapon4");
        Mockito.when(weapon4.isTwoHanded()).thenReturn(false);

        weapon5 = Mockito.mock(Weapon.class);
        Mockito.when(weapon5.getName()).thenReturn("weapon5");
        Mockito.when(weapon5.isTwoHanded()).thenReturn(false);

        weapon6 = Mockito.mock(Weapon.class);
        Mockito.when(weapon6.getName()).thenReturn("weapon6");
        Mockito.when(weapon6.isTwoHanded()).thenReturn(false);

        final Map<Object, Object> config;
        final WeaponRepository repo;

        config = Mockito.mock(Map.class);
        repo = Mockito.mock(WeaponRepository.class);

        service = new DefaultRulesetService(config, repo);
    }

    @Test
    public final void testFilterWeaponOptions_NoTwoHanded() throws Exception {
        final Collection<Weapon> weapons;

        weapons = service.filterWeaponOptions(getNoTwoHandedWeapons(),
                getWeaponOptions());

        Assert.assertEquals(weapons.size(), 2);

        Assert.assertTrue(weapons.contains(weapon2));
        Assert.assertTrue(weapons.contains(weapon6));
    }

    @Test
    public final void testFilterWeaponOptions_TwoHanded() throws Exception {
        final Collection<Weapon> weapons;

        weapons = service.filterWeaponOptions(getTwoHandedWeapons(),
                getWeaponOptions());

        Assert.assertEquals(weapons.size(), 1);

        Assert.assertTrue(weapons.contains(weapon6));
    }

    private final Collection<Weapon> getNoTwoHandedWeapons() {
        final Collection<Weapon> weapons;

        weapons = new LinkedList<>();

        weapons.add(weapon3);
        weapons.add(weapon4);
        weapons.add(weapon5);

        return weapons;
    }

    private final Collection<Weapon> getTwoHandedWeapons() {
        final Collection<Weapon> weapons;

        weapons = new LinkedList<>();

        weapons.add(weapon1);
        weapons.add(weapon4);
        weapons.add(weapon5);

        return weapons;
    }

    private final Collection<Weapon> getWeaponOptions() {
        final Collection<Weapon> weapons;

        weapons = new LinkedList<>();

        weapons.add(weapon2);
        weapons.add(weapon4);
        weapons.add(weapon6);

        return weapons;
    }

}
