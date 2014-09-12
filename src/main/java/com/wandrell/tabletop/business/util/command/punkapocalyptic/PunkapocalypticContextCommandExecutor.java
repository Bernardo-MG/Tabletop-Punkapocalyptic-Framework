package com.wandrell.tabletop.business.util.command.punkapocalyptic;

import com.wandrell.service.application.ApplicationInfoService;
import com.wandrell.service.swing.layout.LayoutService;
import com.wandrell.tabletop.business.service.punkapocalyptic.PunkapocalypticFileService;
import com.wandrell.tabletop.business.service.punkapocalyptic.PunkapocalypticLocalizationService;
import com.wandrell.tabletop.business.service.punkapocalyptic.PunkapocalypticRulesetService;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.ArmorDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.FactionDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.UnitDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.WeaponDAOAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.ApplicationInfoServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.FileServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LayoutServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.LocalizationServiceAware;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.service.RulesetServiceAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.ArmorDAO;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.FactionDAO;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.UnitDAO;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.WeaponDAO;
import com.wandrell.util.command.Command;
import com.wandrell.util.command.CommandExecutor;
import com.wandrell.util.command.ReturnCommand;

public final class PunkapocalypticContextCommandExecutor implements
        CommandExecutor {

    private ArmorDAO                           daoArmor;
    private FactionDAO                         daoFaction;
    private RulesetDAO                         daoSpecialRule;
    private UnitDAO                            daoUnit;
    private WeaponDAO                          daoWeapon;
    private final CommandExecutor              executor;
    private ApplicationInfoService             serviceApplicationInfo;
    private PunkapocalypticFileService         serviceFile;
    private LayoutService                      serviceLayout;
    private PunkapocalypticLocalizationService serviceLocalization;
    private PunkapocalypticRulesetService      serviceRuleset;

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
        serviceApplicationInfo = service;
    }

    public final void setArmorDAO(final ArmorDAO dao) {
        daoArmor = dao;
    }

    public final void setFactionDAO(final FactionDAO dao) {
        daoFaction = dao;
    }

    public final void setFileService(final PunkapocalypticFileService service) {
        serviceFile = service;
    }

    public final void setLayoutService(final LayoutService service) {
        serviceLayout = service;
    }

    public final void setLocalizationService(
            final PunkapocalypticLocalizationService service) {
        serviceLocalization = service;
    }

    public final void setRulesetService(
            final PunkapocalypticRulesetService service) {
        serviceRuleset = service;
    }

    public final void setSpecialRuleDAO(final RulesetDAO dao) {
        daoSpecialRule = dao;
    }

    public final void setUnitDAO(final UnitDAO dao) {
        daoUnit = dao;
    }

    public final void setWeaponDAO(final WeaponDAO dao) {
        daoWeapon = dao;
    }

    private final CommandExecutor getExecutor() {
        return executor;
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

        if (command instanceof ArmorDAOAware) {
            ((ArmorDAOAware) command).setArmorDAO(getArmorDAO());
        }

        if (command instanceof FactionDAOAware) {
            ((FactionDAOAware) command).setFactionDAO(getFactionDAO());
        }

        if (command instanceof RulesetDAOAware) {
            ((RulesetDAOAware) command).setRulesetDAO(getSpecialRuleDAO());
        }

        if (command instanceof UnitDAOAware) {
            ((UnitDAOAware) command).setUnitDAO(getUnitDAO());
        }

        if (command instanceof WeaponDAOAware) {
            ((WeaponDAOAware) command).setWeaponDAO(getWeaponDAO());
        }
    }

    protected final ApplicationInfoService getApplicationInfoService() {
        return serviceApplicationInfo;
    }

    protected final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

    protected final FactionDAO getFactionDAO() {
        return daoFaction;
    }

    protected final PunkapocalypticFileService getFileService() {
        return serviceFile;
    }

    protected final LayoutService getLayoutService() {
        return serviceLayout;
    }

    protected final PunkapocalypticLocalizationService getLocalizationService() {
        return serviceLocalization;
    }

    protected final PunkapocalypticRulesetService getRulesetService() {
        return serviceRuleset;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoSpecialRule;
    }

    protected final UnitDAO getUnitDAO() {
        return daoUnit;
    }

    protected final WeaponDAO getWeaponDAO() {
        return daoWeapon;
    }

}
