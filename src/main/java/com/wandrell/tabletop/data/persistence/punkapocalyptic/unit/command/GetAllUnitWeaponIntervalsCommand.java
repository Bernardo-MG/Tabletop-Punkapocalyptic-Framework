package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.interval.Interval;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitWeaponIntervalXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLDocumentInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitWeaponIntervalsCommand implements
        ReturnCommand<Map<String, Interval>> {

    public GetAllUnitWeaponIntervalsCommand() {
        super();
    }

    @Override
    public final Map<String, Interval> execute() throws Exception {
        final FileParser<Map<String, Interval>> fileWeapons;
        final XMLAdapter<Map<String, Interval>> adapter;
        final XMLDocumentInterpreter<Map<String, Interval>> reader;
        final XMLDocumentValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new UnitWeaponIntervalXMLDocumentReader();
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileWeapons = new DefaultXMLFileParser<>(adapter, reader, validator);

        return fileWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

}
