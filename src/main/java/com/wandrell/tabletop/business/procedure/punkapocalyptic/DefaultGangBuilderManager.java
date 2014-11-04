package com.wandrell.tabletop.business.procedure.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.swing.event.EventListenerList;

import com.wandrell.tabletop.business.model.punkapocalyptic.ruleset.constraint.GangConstraint;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Gang;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.Unit;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListener;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.GangListenerAdapter;
import com.wandrell.tabletop.business.model.punkapocalyptic.unit.event.UnitEvent;
import com.wandrell.tabletop.business.model.valuehandler.AbstractValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ModularDerivedValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.ValueHandler;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerEvent;
import com.wandrell.tabletop.business.model.valuehandler.event.ValueHandlerListener;
import com.wandrell.tabletop.business.model.valuehandler.module.store.AbstractStoreModule;
import com.wandrell.tabletop.business.model.valuehandler.module.store.punkapocalyptic.MaxUnitsStore;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedEvent;
import com.wandrell.tabletop.business.procedure.punkapocalyptic.event.GangChangedListener;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;

public final class DefaultGangBuilderManager implements GangBuilderManager {

    private final Collection<GangConstraint> constraints       = new LinkedHashSet<>();
    private Gang                             gang;
    private final GangListener               gangListener;
    private final EventListenerList          listeners         = new EventListenerList();
    private final ModularDerivedValueHandler maxUnits;
    private final DataModelService           serviceModel;
    private RulesetService                   serviceRuleset;
    private final String                     tooMany;
    private final UnitConfigurationManager   unitConfigManager;
    private String                           validationMessage = "";

    public DefaultGangBuilderManager(
            final UnitConfigurationManager unitConfigManager,
            final String tooManyError, final DataModelService dataModelService,
            final RulesetService rulesetService) {
        super();

        checkNotNull(unitConfigManager, "Received a null pointer as controller");
        checkNotNull(tooManyError, "Received a null pointer as message");
        checkNotNull(dataModelService,
                "Received a null pointer as model service");
        checkNotNull(rulesetService,
                "Received a null pointer as rule set service");

        maxUnits = new ModularDerivedValueHandler("max_units",
                new AbstractStoreModule() {

                    @Override
                    public final AbstractStoreModule createNewInstance() {
                        return this;
                    }

                    @Override
                    public final Integer getValue() {
                        return 0;
                    }

                });

        this.unitConfigManager = unitConfigManager;
        this.tooMany = tooManyError;
        this.serviceModel = dataModelService;
        serviceRuleset = rulesetService;

        gangListener = new GangListenerAdapter() {

            @Override
            public final void unitAdded(final UnitEvent event) {
                getConstraints().addAll(
                        getDataModelService().getUnitConstraints(
                                event.getUnit().getUnitName(),
                                getGang().getFaction().getName()));

                validate();
            }

            @Override
            public final void unitRemoved(final UnitEvent event) {
                getConstraints().clear();
                for (final Unit unit : getGang().getUnits()) {
                    getConstraints().addAll(
                            getDataModelService().getUnitConstraints(
                                    unit.getUnitName(),
                                    getGang().getFaction().getName()));
                }

                validate();
            }

        };
    }

    @Override
    public final void
            addGangChangedListener(final GangChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().add(GangChangedListener.class, listener);
    }

    @Override
    public final Gang getGang() {
        return gang;
    }

    @Override
    public final ValueHandler getMaxUnits() {
        return maxUnits;
    }

    @Override
    public final UnitConfigurationManager getUnitConfigurationController() {
        return unitConfigManager;
    }

    @Override
    public final String getValidationMessage() {
        return validationMessage;
    }

    @Override
    public final void removeGangChangedListener(
            final GangChangedListener listener) {
        checkNotNull(listener, "Received a null pointer as listener");

        getListeners().remove(GangChangedListener.class, listener);
    }

    @Override
    public final void setGang(final Gang gang) {
        checkNotNull(gang, "Received a null pointer as gang");

        fireValueChangedEvent(new GangChangedEvent(this, this.gang, gang));

        gang.removeGangListener(getGangListener());

        this.gang = gang;

        maxUnits.setStore(new MaxUnitsStore(getGang(), getRulesetService()));

        ((AbstractValueHandler) gang.getBullets())
                .addValueEventListener(new ValueHandlerListener() {

                    @Override
                    public final void valueChanged(final ValueHandlerEvent evt) {
                        validate();
                    }

                });

        gang.addGangListener(getGangListener());
    }

    @Override
    public final Boolean validate() {
        final StringBuilder textErrors;
        final Boolean failedCount;
        final Boolean failedConstraints;
        final Boolean failed;

        textErrors = new StringBuilder();

        failedCount = validateUnitsCount(textErrors);

        failedConstraints = validateUnitConstraints(textErrors);

        failed = (failedCount || failedConstraints);

        setValidationMessage(textErrors.toString());

        return !failed;
    }

    private final Collection<GangConstraint> getConstraints() {
        return constraints;
    }

    private final DataModelService getDataModelService() {
        return serviceModel;
    }

    private final GangListener getGangListener() {
        return gangListener;
    }

    private final EventListenerList getListeners() {
        return listeners;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final String getTooManyUnitsWarningMessage() {
        return tooMany;
    }

    private final void setValidationMessage(final String message) {
        validationMessage = message;
    }

    private final Boolean
            validateUnitConstraints(final StringBuilder textErrors) {
        Boolean failed;

        failed = false;
        for (final GangConstraint constraint : getConstraints()) {
            if (!constraint.isValid(getGang())) {
                if (textErrors.length() > 0) {
                    textErrors.append(System.lineSeparator());
                }
                textErrors.append(constraint.getErrorMessage());

                failed = true;
            }
        }

        return failed;
    }

    private final Boolean validateUnitsCount(final StringBuilder textErrors) {
        final Boolean failed;

        if (getGang().getUnits().size() > getMaxUnits().getValue()) {
            textErrors.append(String.format(getTooManyUnitsWarningMessage(),
                    getGang().getUnits().size(), getMaxUnits().getValue()));
            failed = true;
        } else {
            failed = false;
        }

        return failed;
    }

    protected final void fireValueChangedEvent(final GangChangedEvent event) {
        final GangChangedListener[] listnrs;

        checkNotNull(event, "Received a null pointer as event");

        listnrs = getListeners().getListeners(GangChangedListener.class);
        for (final GangChangedListener l : listnrs) {
            l.gangChanged(event);
        }
    }

}
