package com.wandrell.tabletop.data.service.punkapocalyptic.model.command;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelValidationFileConf;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;

public final class ModelInputStreamsCommand implements
        ReturnCommand<Map<InputStream, InputStream>> {

    public ModelInputStreamsCommand() {
        super();
    }

    @Override
    public final Map<InputStream, InputStream> execute() {
        final Map<InputStream, InputStream> sources;

        sources = new LinkedHashMap<>();

        sources.put(ResourceUtils.getClassPathInputStream(ModelFileConf.ARMOR),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.ARMOR));
        sources.put(
                ResourceUtils.getClassPathInputStream(ModelFileConf.EQUIPMENT),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.EQUIPMENT));
        sources.put(ResourceUtils
                .getClassPathInputStream(ModelFileConf.FACTION), ResourceUtils
                .getClassPathInputStream(ModelValidationFileConf.FACTION));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.FACTION_UNITS),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.FACTION_UNITS));
        sources.put(
                ResourceUtils.getClassPathInputStream(ModelFileConf.UNIT_ARMOR),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.UNIT_ARMOR));
        sources.put(ResourceUtils.getClassPathInputStream(ModelFileConf.UNIT),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.UNIT));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.UNIT_ENHANCEMENT),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.UNIT_ENHANCEMENT));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.UNIT_WEAPON),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.UNIT_WEAPON));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.UNIT_EQUIPMENT),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.UNIT_EQUIPMENT));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.WEAPON_ENHANCEMENT),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.WEAPON_ENHANCEMENT));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.WEAPON_MELEE),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.WEAPON_MELEE));
        sources.put(
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.WEAPON_RANGED),
                ResourceUtils
                        .getClassPathInputStream(ModelValidationFileConf.WEAPON_RANGED));

        return sources;
    }

}
