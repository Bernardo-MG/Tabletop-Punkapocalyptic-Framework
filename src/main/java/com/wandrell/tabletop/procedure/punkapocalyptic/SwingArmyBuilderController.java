package com.wandrell.tabletop.procedure.punkapocalyptic;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.wandrell.tabletop.model.punkapocalyptic.ruleset.UnitConstraint;
import com.wandrell.tabletop.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.model.punkapocalyptic.unit.Band;
import com.wandrell.tabletop.model.punkapocalyptic.unit.Unit;

public final class SwingArmyBuilderController implements ArmyBuilderController {

    private final Band                        band;
    private final JTable                      table;
    private final JTextPane                   textErrors;
    private final UnitConfigurationController unitConfig;

    public SwingArmyBuilderController(
            final UnitConfigurationController unitConfig, final Band band,
            final JTable table, final JTextPane textErrors) {
        super();

        this.unitConfig = unitConfig;
        this.band = band;
        this.table = table;
        this.textErrors = textErrors;
    }

    @Override
    public final void addUnit(final AvailabilityUnit unit) {
        final Vector<Object> row;
        StringBuilder textErrors;

        textErrors = new StringBuilder();
        for (final UnitConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getBand())) {
                if (textErrors.toString().length() > 0) {
                    textErrors.append(System.lineSeparator());
                }
                textErrors.append(constraint.getErrorMessage());
            }
        }
        getErrorsPane().setText(textErrors.toString());

        row = new Vector<>(getTable().getRowCount());
        row.add(unit);

        ((DefaultTableModel) getTable().getModel()).addRow(row);
        getBand().addUnit(unit);
    }

    @Override
    public final Band getBand() {
        return band;
    }

    @Override
    public final UnitConfigurationController getUnitConfigurationController() {
        return unitConfig;
    }

    protected final Collection<UnitConstraint> getConstraints() {
        final Collection<UnitConstraint> constraints;

        constraints = new LinkedHashSet<>();

        for (final Unit unit : getBand().getUnits()) {
            constraints.addAll(((AvailabilityUnit) unit).getConstraints());
        }

        return constraints;
    }

    protected final JTextPane getErrorsPane() {
        return textErrors;
    }

    protected final JTable getTable() {
        return table;
    }

}
