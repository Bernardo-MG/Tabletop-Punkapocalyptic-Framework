package com.wandrell.tabletop.business.util.command.punkapocalyptic;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.service.swing.layout.LayoutService;
import com.wandrell.tabletop.business.service.punkapocalyptic.FileService;
import com.wandrell.tabletop.business.service.punkapocalyptic.LocalizationService;
import com.wandrell.tabletop.business.service.punkapocalyptic.RulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.FactionDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.FileServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LayoutServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.FactionDAO;
import com.wandrell.util.command.Command;
import com.wandrell.util.command.CommandExecutor;
import com.wandrell.util.command.ReturnCommand;

public final class PunkapocalypticContextCommandExecutor implements
        CommandExecutor {

    private FactionDAO             daoFaction;
    private final CommandExecutor  executor;
    private ApplicationInfoService serviceApplicationInfo;
    private FileService            serviceFile;
    private LayoutService          serviceLayout;
    private LocalizationService    serviceLocalization;
    private RulesetService         serviceRuleset;

    public PunkapocalypticContextCommandExecutor(final CommandExecutor executor) {
        super();
        this.executor = executor;
    }

    @Override
    public final void execute(final Command command) {
        setContext(command);

        getExecutor().execute(command);
    }

    @Override
    public final <V> V execute(final ReturnCommand<V> command) {
        setContext(command);

        return getExecutor().execute(command);
    }

    public final void setApplicationInfoService(
            final ApplicationInfoService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as application info service");
        }

        serviceApplicationInfo = service;
    }

    public final void setFactionDAO(final FactionDAO dao) {
        if (dao == null) {
            throw new NullPointerException(
                    "Received a null pointer as faction DAO");
        }

        daoFaction = dao;
    }

    public final void setFileService(final FileService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as file service");
        }

        serviceFile = service;
    }

    public final void setLayoutService(final LayoutService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as layout service");
        }

        serviceLayout = service;
    }

    public final void setLocalizationService(final LocalizationService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as localization service");
        }

        serviceLocalization = service;
    }

    public final void setRulesetService(final RulesetService service) {
        if (service == null) {
            throw new NullPointerException(
                    "Received a null pointer as ruleset service");
        }

        serviceRuleset = service;
    }

    private final ApplicationInfoService getApplicationInfoService() {
        return serviceApplicationInfo;
    }

    private final CommandExecutor getExecutor() {
        return executor;
    }

    private final FactionDAO getFactionDAO() {
        return daoFaction;
    }

    private final FileService getFileService() {
        return serviceFile;
    }

    private final LayoutService getLayoutService() {
        return serviceLayout;
    }

    private final LocalizationService getLocalizationService() {
        return serviceLocalization;
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

        if (command instanceof FactionDAOAware) {
            ((FactionDAOAware) command).setFactionDAO(getFactionDAO());
        }
    }

}
