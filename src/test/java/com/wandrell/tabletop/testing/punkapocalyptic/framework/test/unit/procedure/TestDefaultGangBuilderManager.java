package com.wandrell.tabletop.testing.punkapocalyptic.framework.test.unit.procedure;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.wandrell.pattern.repository.QueryableRepository;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.procedure.ConstraintValidator;
import com.wandrell.tabletop.punkapocalyptic.model.availability.FactionUnitAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.GangListener;
import com.wandrell.tabletop.punkapocalyptic.model.unit.event.UnitEvent;
import com.wandrell.tabletop.punkapocalyptic.procedure.DefaultGangBuilderManager;
import com.wandrell.tabletop.punkapocalyptic.service.RulesetService;

public final class TestDefaultGangBuilderManager {

    public TestDefaultGangBuilderManager() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testAddUnit_UpdatesConstraints() {
        final Gang gang;
        final Unit unit1;
        final Collection<Unit> units;
        final ArgumentCaptor<GangListener> listenerCaptor;
        final ArgumentCaptor<Constraint> constraintCaptor;
        final GangListener listenerGang;
        final Collection<FactionUnitAvailability> availabilities;
        final FactionUnitAvailability ava;
        final Collection<Constraint> constraints;
        final Constraint constraint;
        final Iterator<Constraint> itrConstraints;
        final DefaultGangBuilderManager manager;
        final ConstraintValidator validator;
        final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository;
        final RulesetService rulesetService;

        unitAvaRepository = Mockito.mock(QueryableRepository.class);
        rulesetService = Mockito.mock(RulesetService.class);

        validator = Mockito.mock(ConstraintValidator.class);

        manager = new DefaultGangBuilderManager(validator, unitAvaRepository,
                rulesetService);

        listenerCaptor = ArgumentCaptor.forClass(GangListener.class);
        constraintCaptor = ArgumentCaptor.forClass(Constraint.class);

        constraints = new LinkedList<>();

        constraint = Mockito.mock(Constraint.class);
        Mockito.when(constraint.isValid()).thenReturn(false);
        Mockito.when(constraint.toString()).thenReturn("MockedConstraint1");

        constraints.add(constraint);

        ava = Mockito.mock(FactionUnitAvailability.class);

        Mockito.when(ava.getConstraints()).thenReturn(constraints);

        availabilities = new LinkedList<>();
        availabilities.add(ava);

        Mockito.when(
                unitAvaRepository.getCollection(Matchers.any(Predicate.class)))
                .thenReturn(availabilities);

        gang = Mockito.mock(Gang.class);

        unit1 = Mockito.mock(Unit.class);

        units = new LinkedList<>();

        Mockito.when(gang.getUnits()).thenReturn(units);

        manager.setGang(gang);

        Mockito.verify(gang, Mockito.atLeast(1)).addGangListener(
                listenerCaptor.capture());

        listenerGang = listenerCaptor.getValue();

        listenerGang.unitAdded(new UnitEvent(this, unit1));

        Mockito.verify(validator, Mockito.atLeastOnce()).addConstraint(
                constraintCaptor.capture());

        Assert.assertEquals(constraintCaptor.getAllValues().size(), 2);

        itrConstraints = constraintCaptor.getAllValues().iterator();
        itrConstraints.next();
        Assert.assertEquals(itrConstraints.next(), constraint);
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testRemoveUnit_UpdatesConstraints() {
        final Gang gang;
        final Unit unit1;
        final Collection<Unit> units;
        final ArgumentCaptor<GangListener> listenerCaptor;
        final ArgumentCaptor<Constraint> constraintCaptor;
        final GangListener listenerGang;
        final Collection<FactionUnitAvailability> availabilities;
        final FactionUnitAvailability ava;
        final Collection<Constraint> constraints;
        final Constraint constraint;
        final DefaultGangBuilderManager manager;
        final ConstraintValidator validator;
        final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository;
        final RulesetService rulesetService;

        unitAvaRepository = Mockito.mock(QueryableRepository.class);
        rulesetService = Mockito.mock(RulesetService.class);

        validator = Mockito.mock(ConstraintValidator.class);

        manager = new DefaultGangBuilderManager(validator, unitAvaRepository,
                rulesetService);

        listenerCaptor = ArgumentCaptor.forClass(GangListener.class);
        constraintCaptor = ArgumentCaptor.forClass(Constraint.class);

        constraints = new LinkedList<>();

        constraint = Mockito.mock(Constraint.class);
        Mockito.when(constraint.isValid()).thenReturn(false);
        Mockito.when(constraint.toString()).thenReturn("MockedConstraint1");

        constraints.add(constraint);

        ava = Mockito.mock(FactionUnitAvailability.class);

        Mockito.when(ava.getConstraints()).thenReturn(constraints);

        availabilities = new LinkedList<>();
        availabilities.add(ava);

        Mockito.when(
                unitAvaRepository.getCollection(Matchers.any(Predicate.class)))
                .thenReturn(availabilities);

        gang = Mockito.mock(Gang.class);

        unit1 = Mockito.mock(Unit.class);

        units = new LinkedList<>();
        units.add(unit1);

        Mockito.when(gang.getUnits()).thenReturn(units);

        manager.setGang(gang);

        Mockito.verify(gang, Mockito.atLeast(1)).addGangListener(
                listenerCaptor.capture());

        listenerGang = listenerCaptor.getValue();

        listenerGang.unitRemoved(new UnitEvent(this, unit1));

        Mockito.verify(validator, Mockito.atLeastOnce()).addConstraint(
                constraintCaptor.capture());

        // Assert.assertEquals(constraintCaptor.getAllValues().size(), 2);

        Assert.assertTrue(constraintCaptor.getAllValues().contains(constraint));
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testValidate_NoValidates() {
        final DefaultGangBuilderManager manager;
        final ConstraintValidator validator;
        final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository;
        final RulesetService rulesetService;

        unitAvaRepository = Mockito.mock(QueryableRepository.class);
        rulesetService = Mockito.mock(RulesetService.class);

        validator = Mockito.mock(ConstraintValidator.class);

        manager = new DefaultGangBuilderManager(validator, unitAvaRepository,
                rulesetService);

        Mockito.when(validator.validate()).thenReturn(false);
        Assert.assertTrue(!manager.validate());
    }

    @SuppressWarnings("unchecked")
    @Test
    public final void testValidate_Validates() {
        final DefaultGangBuilderManager manager;
        final ConstraintValidator validator;
        final QueryableRepository<FactionUnitAvailability, Predicate<FactionUnitAvailability>> unitAvaRepository;
        final RulesetService rulesetService;

        unitAvaRepository = Mockito.mock(QueryableRepository.class);
        rulesetService = Mockito.mock(RulesetService.class);

        validator = Mockito.mock(ConstraintValidator.class);

        manager = new DefaultGangBuilderManager(validator, unitAvaRepository,
                rulesetService);

        Mockito.when(validator.validate()).thenReturn(true);
        Assert.assertTrue(manager.validate());
    }

}
