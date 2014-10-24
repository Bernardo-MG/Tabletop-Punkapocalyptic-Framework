package com.wandrell.tabletop.business.util.command.punkapocalyptic;

import static com.google.common.base.Preconditions.checkNotNull;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.service.swing.layout.LayoutService;
import com.wandrell.tabletop.business.service.punkapocalyptic.FileService;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.DataModelServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.FileServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LayoutServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.tabletop.data.service.punkapocalyptic.model.DataModelService;
import com.wandrell.util.command.Command;
import com.wandrell.util.command.CommandExecutor;
import com.wandrell.util.command.ReturnCommand;

public final class PunkapocalypticContextCommandExecutor implements
        CommandExecutor {

    private final CommandExecutor  executor;
    private ApplicationInfoService serviceAppInfo;
    private DataModelService       serviceDataModel;
    private FileService            serviceFile;
    private LayoutService          serviceLayout;
    private LocalizationService    serviceLoc;
    private RulesetService         serviceRuleset;

    public PunkapocalypticContextCommandExecutor(final CommandExecutor executor) {
        super();

        checkNotNull(executor, "Received a null pointer as base executor");

        this.executor = executor;
    }

    @Override
    public final void execute(final Command command) {
        checkNotNull(command, "Received a null pointer as command");

        setContext(command);

        getExecutor().execute(command);
    }

    @Override
    public final <V> V execute(final ReturnCommand<V> command) {
        checkNotNull(command, "Received a null pointer as command");

        setContext(command);

        return getExecutor().execute(command);
    }

    public final void setApplicationInfoService(
            final ApplicationInfoService service) {
        checkNotNull(service,
                "Received a null pointer as application info service");

        serviceAppInfo = service;
    }

    public final void setDataModelService(final DataModelService service) {
        checkNotNull(service,
                "Received a null pointer as the model data service");

        serviceDataModel = service;
    }

    public final void setFileService(final FileService service) {
        checkNotNull(service, "Received a null pointer as file service");

        serviceFile = service;
    }

    public final void setLayoutService(final LayoutService service) {
        checkNotNull(service, "Received a null pointer as layout service");

        serviceLayout = service;
    }

    public final void setLocalizationService(final LocalizationService service) {
        checkNotNull(service, "Received a null pointer as localization service");

        serviceLoc = service;
    }

    public final void setRulesetService(final RulesetService service) {
        checkNotNull(service, "Received a null pointer as ruleset service");

        serviceRuleset = service;
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return serviceAppInfo;
    }

    private final DataModelService getDataModelService() {
        return serviceDataModel;
    }

    private final CommandExecutor getExecutor() {
        return executor;
    }

    private final FileService getFileService() {
        return serviceFile;
    }

    private final LayoutService getLayoutService() {
        return serviceLayout;
    }

    private final LocalizationService getLocalizationService() {
        return serviceLoc;
    }

    private final RulesetService getRulesetService() {
        return serviceRuleset;
    }

    private final void setContext(final Object command) {
        if (command instanceof ApplicationInfoServiceAware) {
            ((ApplicationInfoServiceAware) command)
                    .setApplicationInfoService(getApplicationInfoService());
        }

        if (command instanceof LocalizationServiceAware) {
            ((LocalizationServiceAware) command)
                    .setLocalizationService(getLocalizationService());
        }

        if (command instanceof LayoutServiceAware) {
            ((LayoutServiceAware) command).setLayoutService(getLayoutService());
        }

        if (command instanceof RulesetServiceAware) {
            ((RulesetServiceAware) command)
                    .setRulesetService(getRulesetService());
        }

        if (command instanceof FileServiceAware) {
            ((FileServiceAware) command).setFileService(getFileService());
        }

        if (command instanceof DataModelServiceAware) {
            ((DataModelServiceAware) command)
                    .setDataModelService(getDataModelService());
        }
    }

}
