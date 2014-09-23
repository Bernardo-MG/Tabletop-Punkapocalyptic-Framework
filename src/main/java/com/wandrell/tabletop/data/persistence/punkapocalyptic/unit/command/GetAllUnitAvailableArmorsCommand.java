package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableArmorsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLDocumentInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitAvailableArmorsCommand implements
        ReturnCommand<Map<String, Collection<Armor>>> {

    private final Map<String, Armor> armors;

    public GetAllUnitAvailableArmorsCommand(final Map<String, Armor> armors) {
        super();

        this.armors = armors;
    }

    @Override
    public final Map<String, Collection<Armor>> execute() throws Exception {
        final FileParser<Map<String, Collection<Armor>>> fileUnitArmors;
        final XMLAdapter<Map<String, Collection<Armor>>> adapter;
        final XMLDocumentInterpreter<Map<String, Collection<Armor>>> reader;
        final XMLDocumentValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new UnitAvailableArmorsXMLDocumentReader(getArmors());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitArmors = new DefaultXMLFileParser<>(adapter, reader, validator);

        return fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    private final Map<String, Armor> getArmors() {
        return armors;
    }

}
