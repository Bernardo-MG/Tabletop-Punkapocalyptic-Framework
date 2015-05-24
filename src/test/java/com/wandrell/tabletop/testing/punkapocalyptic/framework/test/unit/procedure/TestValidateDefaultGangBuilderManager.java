package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.procedure;

import java.util.Collection;
import java.util.LinkedList;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.faction.Faction;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.DefaultGangBuilderManager;
import com.wandrell.tabletop.punkapocalyptic.procedure.GangBuilderManager;
import com.wandrell.tabletop.punkapocalyptic.repository.FactionUnitAvailabilityRepository;
import com.wandrell.tabletop.punkapocalyptic.service.ModelService;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class TestValidateDefaultGangBuilderManager {

    public TestValidateDefaultGangBuilderManager() {
        super();
    }

    @Test
    public final void testValidate_Default() {
        final GangBuilderManager manager;
        final FactionUnitAvailabilityRepository repoAva;
        final RulesetService serviceRuleset;
        final ModelService modelService;

        repoAva = Mockito.mock(FactionUnitAvailabilityRepository.class);
        serviceRuleset = Mockito.mock(RulesetService.class);
        modelService = Mockito.mock(ModelService.class);

        manager = new DefaultGangBuilderManager(repoAva, serviceRuleset,
                modelService);

        Assert.assertEquals(manager.validate(), (Boolean) true);
    }

    @Test
    public final void testValidate_NoValidates() {
        final GangBuilderManager manager;
        final FactionUnitAvailabilityRepository repoAva;
        final RulesetService serviceRuleset;
        final ModelService modelService;
        final Gang gang;
        final Faction faction;
        final Unit unit;
        final Collection<Unit> units;
        final Constraint constraint;

        repoAva = Mockito.mock(FactionUnitAvailabilityRepository.class);
        serviceRuleset = Mockito.mock(RulesetService.class);
        modelService = Mockito.mock(ModelService.class);

        manager = new DefaultGangBuilderManager(repoAva, serviceRuleset,
                modelService);

        faction = Mockito.mock(Faction.class);
        Mockito.when(faction.getNameToken()).thenReturn("faction");

        gang = Mockito.mock(Gang.class);
        Mockito.when(gang.getFaction()).thenReturn(faction);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getName()).thenReturn("unit");

        units = new LinkedList<>();
        units.add(unit);

        Mockito.when(gang.getUnits()).thenReturn(units);

        manager.setGang(gang);

        constraint = Mockito.mock(Constraint.class);
        Mockito.when(constraint.isValid()).thenReturn(false);

        Mockito.when(
                modelService.getConstraint(Matchers.any(Gang.class),
                        Matchers.anyString(), Matchers.anyString(),
                        Matchers.anyCollectionOf(String.class))).thenReturn(
                constraint);

        Assert.assertEquals(manager.validate(), (Boolean) false);
    }

    @Test
    public final void testValidate_NoValidates_Messages() {
        final GangBuilderManager manager;
        final FactionUnitAvailabilityRepository repoAva;
        final RulesetService serviceRuleset;
        final ModelService modelService;
        final Gang gang;
        final Faction faction;
        final Unit unit;
        final Collection<Unit> units;
        final Constraint constraint;

        repoAva = Mockito.mock(FactionUnitAvailabilityRepository.class);
        serviceRuleset = Mockito.mock(RulesetService.class);
        modelService = Mockito.mock(ModelService.class);

        manager = new DefaultGangBuilderManager(repoAva, serviceRuleset,
                modelService);

        faction = Mockito.mock(Faction.class);
        Mockito.when(faction.getNameToken()).thenReturn("faction");

        gang = Mockito.mock(Gang.class);
        Mockito.when(gang.getFaction()).thenReturn(faction);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getName()).thenReturn("unit");

        units = new LinkedList<>();
        units.add(unit);

        Mockito.when(gang.getUnits()).thenReturn(units);

        manager.setGang(gang);

        constraint = Mockito.mock(Constraint.class);
        Mockito.when(constraint.isValid()).thenReturn(false);
        Mockito.when(constraint.getErrorMessage()).thenReturn("message");

        Mockito.when(
                modelService.getConstraint(Matchers.any(Gang.class),
                        Matchers.anyString(), Matchers.anyString(),
                        Matchers.anyCollectionOf(String.class))).thenReturn(
                constraint);

        manager.validate();
        Assert.assertEquals(manager.getValidationMessages().size(), 1);
    }

    @Test
    public final void testValidate_Validates() {
        final GangBuilderManager manager;
        final FactionUnitAvailabilityRepository repoAva;
        final RulesetService serviceRuleset;
        final ModelService modelService;
        final Gang gang;
        final Faction faction;
        final Unit unit;
        final Collection<Unit> units;
        final Constraint constraint;

        repoAva = Mockito.mock(FactionUnitAvailabilityRepository.class);
        serviceRuleset = Mockito.mock(RulesetService.class);
        modelService = Mockito.mock(ModelService.class);

        manager = new DefaultGangBuilderManager(repoAva, serviceRuleset,
                modelService);

        faction = Mockito.mock(Faction.class);
        Mockito.when(faction.getNameToken()).thenReturn("faction");

        gang = Mockito.mock(Gang.class);
        Mockito.when(gang.getFaction()).thenReturn(faction);

        unit = Mockito.mock(Unit.class);
        Mockito.when(unit.getName()).thenReturn("unit");

        units = new LinkedList<>();
        units.add(unit);

        Mockito.when(gang.getUnits()).thenReturn(units);

        manager.setGang(gang);

        constraint = Mockito.mock(Constraint.class);
        Mockito.when(constraint.isValid()).thenReturn(true);

        Mockito.when(
                modelService.getConstraint(Matchers.any(Gang.class),
                        Matchers.anyString(), Matchers.anyString(),
                        Matchers.anyCollectionOf(String.class))).thenReturn(
                constraint);

        Assert.assertEquals(manager.validate(), (Boolean) false);
    }

}
