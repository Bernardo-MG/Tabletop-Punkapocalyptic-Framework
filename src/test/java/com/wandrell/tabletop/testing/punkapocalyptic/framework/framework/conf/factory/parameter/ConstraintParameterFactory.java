package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.parser.xml.FilteredEntriesXMLFileParser;
import com.wandrell.pattern.repository.Repository;
import com.wandrell.pattern.repository.Repository.Filter;
import com.wandrell.tabletop.procedure.Constraint;
import com.wandrell.tabletop.punkapocalyptic.model.availability.UnitWeaponAvailability;
import com.wandrell.tabletop.punkapocalyptic.model.inventory.Weapon;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Gang;
import com.wandrell.tabletop.punkapocalyptic.model.unit.Unit;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.DependantUnitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.GangUnitsUpToLimitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToACountConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitUpToHalfGangLimitConstraint;
import com.wandrell.tabletop.punkapocalyptic.procedure.constraint.UnitWeaponsInIntervalConstraint;
import com.wandrell.tabletop.punkapocalyptic.util.tag.GangAware;
import com.wandrell.tabletop.punkapocalyptic.util.tag.UnitAware;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.ConstraintParametersConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.DependantDocumentParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.UnitLimitDocumentParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.UpToCountDocumentParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.UpToHalfDocumentParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.WeaponIntervalDocumentParser;
import com.wandrell.tabletop.valuebox.ValueBox;
import com.wandrell.util.ResourceUtils;

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

    public final Iterator<Object[]> getNotValidDependantConstraintParameters()
            throws Exception {
        return getDependantConstraintParameters(getValues(
                ConstraintParametersConf.DEPENDANT, false,
                new DependantDocumentParser()));
    }

    public final Iterator<Object[]> getNotValidUnitLimitConstraintParameters()
            throws Exception {
        return getUnitLimitConstraintParameters(getValues(
                ConstraintParametersConf.UNIT_LIMIT, false,
                new UnitLimitDocumentParser()));
    }

    public final Iterator<Object[]> getNotValidUpToCountConstraintParameters()
            throws Exception {
        return getUpToCountConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_A_COUNT, false,
                new UpToCountDocumentParser()));
    }

    public final Iterator<Object[]> getNotValidUpToHalfConstraintParameters()
            throws Exception {
        return getUpToHalfConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_HALF, false,
                new UpToHalfDocumentParser()));
    }

    public final Iterator<Object[]>
            getNotValidWeaponIntervalConstraintParameters() throws Exception {
        return getWeaponIntervalConstraintParameters(getValues(
                ConstraintParametersConf.WEAPON_INTERVAL, false,
                new WeaponIntervalDocumentParser()));
    }

    public final Iterator<Object[]> getValidDependantConstraintParameters()
            throws Exception {
        return getDependantConstraintParameters(getValues(
                ConstraintParametersConf.DEPENDANT, true,
                new DependantDocumentParser()));
    }

    public final Iterator<Object[]> getValidUnitLimitConstraintParameters()
            throws Exception {
        return getUnitLimitConstraintParameters(getValues(
                ConstraintParametersConf.UNIT_LIMIT, true,
                new UnitLimitDocumentParser()));
    }

    public final Iterator<Object[]> getValidUpToCountConstraintParameters()
            throws Exception {
        return getUpToCountConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_A_COUNT, true,
                new UpToCountDocumentParser()));
    }

    public final Iterator<Object[]> getValidUpToHalfConstraintParameters()
            throws Exception {
        return getUpToHalfConstraintParameters(getValues(
                ConstraintParametersConf.UP_TO_HALF, true,
                new UpToHalfDocumentParser()));
    }

    public final Iterator<Object[]>
            getValidWeaponIntervalConstraintParameters() throws Exception {
        return getWeaponIntervalConstraintParameters(getValues(
                ConstraintParametersConf.WEAPON_INTERVAL, true,
                new WeaponIntervalDocumentParser()));
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

    private final
            Collection<Collection<Object>>
            getValues(
                    final String file,
                    final Boolean valid,
                    final Parser<Document, Collection<Collection<Object>>> parserParams)
                    throws Exception {
        final FilteredEntriesXMLFileParser parserFile;

        parserFile = new FilteredEntriesXMLFileParser("data");

        if (valid) {
            parserFile.addRequiredAttribute("valid");
        } else {
            parserFile.addRejectedAttribute("valid");
        }

        return parserParams.parse(parserFile.parse(ResourceUtils
                .getClassPathReader(file)));
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
        Repository<UnitWeaponAvailability> repo;
        Collection<Weapon> weaponsCol;
        Collection<UnitWeaponAvailability> avas;
        UnitWeaponAvailability ava;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            itrValues = values.iterator();

            weapons = (Integer) itrValues.next();
            min = (Integer) itrValues.next();
            max = (Integer) itrValues.next();

            ava = Mockito.mock(UnitWeaponAvailability.class);
            Mockito.when(ava.getMinWeapons()).thenReturn(min);
            Mockito.when(ava.getMaxWeapons()).thenReturn(max);

            avas = new LinkedList<>();
            avas.add(ava);

            repo = Mockito.mock(Repository.class);
            Mockito.when(repo.getCollection(Matchers.any(Filter.class)))
                    .thenReturn(avas);

            weaponsCol = Mockito.mock(Collection.class);

            Mockito.when(weaponsCol.size()).thenReturn(weapons);

            unit = Mockito.mock(Unit.class);
            Mockito.when(unit.getWeapons()).thenReturn(weaponsCol);

            constraint = new UnitWeaponsInIntervalConstraint(repo, "message");

            ((UnitAware) constraint).setUnit(unit);

            result.add(new Object[] { constraint });
        }

        return result.iterator();
    }

}
