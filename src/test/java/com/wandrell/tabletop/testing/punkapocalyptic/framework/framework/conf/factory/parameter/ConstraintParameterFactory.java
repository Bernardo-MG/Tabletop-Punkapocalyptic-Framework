package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import com.wandrell.conf.TestingConf;
import com.wandrell.tabletop.business.model.interval.DefaultInterval;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuebox.ValueBox;
import com.wandrell.tabletop.business.procedure.Constraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.DependantUnitConstraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.GangUnitsUpToLimitConstraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.UnitUpToACountConstraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.business.procedure.constraint.punkapocalyptic.UnitWeaponsInIntervalConstraint;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.GangAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.UnitAware;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataService;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.ConstraintParametersConf;
import com.wandrell.util.ContextUtils;
import com.wandrell.util.FileUtils;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.TestUtils;

public final class ConstraintParameterFactory {

    private static final ConstraintParameterFactory instance = new ConstraintParameterFactory();
    private static final Integer                    MAX      = 20;
    private static final String                     UNIT1    = "unit1";
    private static final String                     UNIT2    = "unit2";
    private static final String                     UNIT3    = "unit3";

    public static final ConstraintParameterFactory getInstance() {
        return instance;
    }

    private ConstraintParameterFactory() {
        super();
    }

    public final Iterator<Object[]> getNotValidDependantConstraintParameters() {
        return getDependantConstraintParameters(getValues(
                ConstraintParametersConf.DEPENDANT_PROPERTIES, false));
    }

    public final Iterator<Object[]> getNotValidUnitLimitConstraintParameters() {
        return getUnitLimitConstraintParameters(getValues(
                ConstraintParametersConf.UNIT_LIMIT_PROPERTIES, false));
    }

    public final Iterator<Object[]> getNotValidUpToCountConstraintParameters() {
        return getUpToCountConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_A_COUNT_PROPERTIES, false));
    }

    public final Iterator<Object[]> getNotValidUpToHalfConstraintParameters() {
        return getUpToHalfConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_HALF_PROPERTIES, false));
    }

    public final Iterator<Object[]>
            getNotValidWeaponIntervalConstraintParameters() {
        return getWeaponIntervalConstraintParameters(getValues(
                ConstraintParametersConf.WEAPON_INTERVAL_PROPERTIES, false));
    }

    public final Iterator<Object[]> getValidDependantConstraintParameters() {
        return getDependantConstraintParameters(getValues(
                ConstraintParametersConf.DEPENDANT_PROPERTIES, true));
    }

    public final Iterator<Object[]> getValidUnitLimitConstraintParameters() {
        return getUnitLimitConstraintParameters(getValues(
                ConstraintParametersConf.UNIT_LIMIT_PROPERTIES, true));
    }

    public final Iterator<Object[]> getValidUpToCountConstraintParameters() {
        return getUpToCountConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_A_COUNT_PROPERTIES, true));
    }

    public final Iterator<Object[]> getValidUpToHalfConstraintParameters() {
        return getUpToHalfConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_HALF_PROPERTIES, true));
    }

    public final Iterator<Object[]>
            getValidWeaponIntervalConstraintParameters() {
        return getWeaponIntervalConstraintParameters(getValues(
                ConstraintParametersConf.WEAPON_INTERVAL_PROPERTIES, true));
    }

    private final Iterator<Object[]> getDependantConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        List<Unit> units;
        Constraint constraint;
        Gang gang;
        Unit unit;
        Integer limit;
        Integer dependant;
        Integer master;
        Integer range;
        final Integer total = 10;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            dependant = (Integer) itrValues.next();
            master = (Integer) itrValues.next();
            range = (Integer) itrValues.next();

            constraint = new DependantUnitConstraint(UNIT1, UNIT2, range,
                    "message");

            units = new LinkedList<>();
            for (Integer i = 0; i < dependant; i++) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(UNIT2);
                Mockito.when(unit.toString()).thenReturn(UNIT2);

                units.add(unit);
            }

            for (Integer i = 0; i < master; i++) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(UNIT1);
                Mockito.when(unit.toString()).thenReturn(UNIT1);

                units.add(unit);
            }

            limit = total - units.size();
            for (Integer i = 0; i < limit; i++) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(UNIT3);
                Mockito.when(unit.toString()).thenReturn(UNIT3);

                units.add(unit);
            }

            Collections.shuffle(units);

            gang = Mockito.mock(Gang.class);

            Mockito.when(gang.getUnits()).thenReturn(units);

            ((GangAware) constraint).setGang(gang);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

    @SuppressWarnings("unchecked")
    private final Iterator<Object[]> getUnitLimitConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        Constraint constraint;
        ValueBox limit;
        Integer valueLimit;
        Integer unitsCount;
        Collection<Unit> units;
        Gang gang;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            valueLimit = (Integer) itrValues.next();
            limit = Mockito.mock(ValueBox.class);
            Mockito.when(limit.getValue()).thenReturn(valueLimit);
            Mockito.when(limit.toString()).thenReturn(valueLimit.toString());

            constraint = new GangUnitsUpToLimitConstraint(limit, "message");

            gang = Mockito.mock(Gang.class);

            unitsCount = (Integer) itrValues.next();
            units = Mockito.mock(Collection.class);
            Mockito.when(units.size()).thenReturn(unitsCount);
            Mockito.when(gang.getUnits()).thenReturn(units);
            Mockito.when(gang.toString()).thenReturn(
                    String.format("Gang with %d units", unitsCount));

            ((GangAware) constraint).setGang(gang);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

    private final Iterator<Object[]> getUpToCountConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        List<Unit> units;
        Constraint constraint;
        Gang gang;
        Unit unit;
        Integer valid;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            constraint = new UnitUpToACountConstraint(UNIT1,
                    (Integer) itrValues.next(), "message");

            valid = (Integer) itrValues.next();

            units = new LinkedList<>();
            for (Integer i = 0; i < MAX; i++) {
                unit = Mockito.mock(Unit.class);

                if (i < valid) {
                    Mockito.when(unit.getUnitName()).thenReturn(UNIT1);
                } else {
                    Mockito.when(unit.getUnitName()).thenReturn(UNIT2);
                }

                units.add(unit);
            }

            Collections.shuffle(units);

            gang = Mockito.mock(Gang.class);

            Mockito.when(gang.getUnits()).thenReturn(units);
            Mockito.when(gang.toString()).thenReturn(
                    String.format("Gang with %d valid units", valid));

            ((GangAware) constraint).setGang(gang);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

    private final Iterator<Object[]> getUpToHalfConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        List<Unit> units;
        Constraint constraint;
        Gang gang;
        Unit unit;
        Integer valid;
        Integer total;
        Integer limit;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            constraint = new UnitUpToHalfGangLimitConstraint(UNIT1, "message");

            valid = (Integer) itrValues.next();
            total = (Integer) itrValues.next();

            units = new LinkedList<>();
            for (Integer i = 0; i < valid; i++) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(UNIT1);

                units.add(unit);
            }

            limit = total - valid;
            for (Integer i = 0; i < limit; i++) {
                unit = Mockito.mock(Unit.class);

                Mockito.when(unit.getUnitName()).thenReturn(UNIT2);

                units.add(unit);
            }

            Collections.shuffle(units);

            gang = Mockito.mock(Gang.class);

            Mockito.when(gang.getUnits()).thenReturn(units);
            Mockito.when(gang.toString()).thenReturn(
                    String.format("Gang with %d valid units in a total of %d",
                            valid, total));

            ((GangAware) constraint).setGang(gang);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

    private final Collection<Collection<Object>> getValues(final String file,
            final Boolean valid) {
        final ApplicationContext context;
        final Properties properties;
        final Collection<String> required;
        final Collection<String> rejected;

        properties = FileUtils.getProperties(ResourceUtils
                .getClassPathInputStream(file));
        context = ContextUtils.getClassPathContext(properties,
                TestingConf.CONTEXT_FILTERED);

        required = new LinkedList<>();
        rejected = new LinkedList<>();

        if (valid) {
            required.add("valid");
        } else {
            rejected.add("valid");
        }

        return TestUtils.getParameters(context, required, rejected);
    }

    @SuppressWarnings("unchecked")
    private final Iterator<Object[]> getWeaponIntervalConstraintParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;
        Iterator<Object> itrValues;
        Constraint constraint;
        Unit unit;
        Integer weapons;
        Integer min;
        Integer max;
        DataService service;
        Interval interval;
        Collection<Weapon> weaponsCol;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            weapons = (Integer) itrValues.next();
            min = (Integer) itrValues.next();
            max = (Integer) itrValues.next();

            interval = new DefaultInterval(min, max);

            service = Mockito.mock(DataService.class);
            Mockito.when(
                    service.getUnitAllowedWeaponsInterval(Matchers.anyString()))
                    .thenReturn(interval);

            weaponsCol = Mockito.mock(Collection.class);

            Mockito.when(weaponsCol.size()).thenReturn(weapons);

            unit = Mockito.mock(Unit.class);
            Mockito.when(unit.getWeapons()).thenReturn(weaponsCol);

            constraint = new UnitWeaponsInIntervalConstraint(service, "message");

            ((UnitAware) constraint).setUnit(unit);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

}
