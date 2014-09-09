package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitWeaponIntervalXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllUnitWeaponIntervalsCommand implements
        ReturnCommand<Map<String, Interval>> {

    public GetAllUnitWeaponIntervalsCommand() {
        super();
    }

    @Override
    public final Map<String, Interval> execute() {
        final FileHandler<Map<String, Interval>> fileWeapons;
        final Map<String, Interval> weapons;

        fileWeapons = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Interval>>(),
                new UnitWeaponIntervalXMLDocumentReader(),
                new XSDValidator(
                        ModelFile.VALIDATION_UNIT_AVAILABILITY,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        weapons = fileWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFile.UNIT_AVAILABILITY));

        return weapons;
    }

}
