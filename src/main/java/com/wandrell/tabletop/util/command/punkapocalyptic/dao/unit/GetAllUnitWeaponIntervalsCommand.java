package com.wandrell.tabletop.util.command.punkapocalyptic.dao.unit;

import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.interval.Interval;
import com.wandrell.tabletop.util.file.punkapocalyptic.unit.UnitWeaponIntervalXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

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
