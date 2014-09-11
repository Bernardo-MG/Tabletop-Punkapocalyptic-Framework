package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.JTextPane;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.ArmyBuilderUnitConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.AvailabilityUnit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.valuehandler.AbstractValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;

public final class DefaultArmyBuilderController implements
        ArmyBuilderController {

    private final Gang                        band;
    private final JTextPane                   textErrors;
    private final UnitConfigurationController unitConfig;

    public DefaultArmyBuilderController(
            final UnitConfigurationController unitConfig, final Gang band,
            final JTextPane textErrors) {
        super();

        this.unitConfig = unitConfig;
        this.band = band;
        this.textErrors = textErrors;

        ((AbstractValueHandler) band.getBullets())
                .addValueEventListener(new ValueHandlerListener() {

                    @Override
                    public final void valueChanged(final ValueHandlerEvent evt) {
                        validate();
                    }

                });
    }

    @Override
    public final void addUnit(final AvailabilityUnit unit) {
        getBand().addUnit(unit);

        validate();
    }

    @Override
    public final Gang getBand() {
        return band;
    }

    @Override
    public final UnitConfigurationController getUnitConfigurationController() {
        return unitConfig;
    }

    @Override
    public final void removeUnit(final AvailabilityUnit unit) {
        getBand().removeUnit(unit);
    }

    protected final Collection<ArmyBuilderUnitConstraint> getConstraints() {
        final Collection<ArmyBuilderUnitConstraint> constraints;

        constraints = new LinkedHashSet<>();

        for (final Unit unit : getBand().getUnits()) {
            constraints.addAll(((AvailabilityUnit) unit).getConstraints());
        }

        return constraints;
    }

    protected final JTextPane getErrorsPane() {
        return textErrors;
    }

    protected final void validate() {
        StringBuilder textErrors;

        textErrors = new StringBuilder();
        for (final ArmyBuilderUnitConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getBand())) {
                if (textErrors.toString().length() > 0) {
                    textErrors.append(System.lineSeparator());
                }
                textErrors.append(constraint.getErrorMessage());
            }
        }

        getErrorsPane().setText(textErrors.toString());
    }

}
