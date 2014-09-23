package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableWeaponsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileParser;
import com.wandrell.util.file.xml.DefaultXMLFileParser;
import com.wandrell.util.file.xml.module.adapter.JDOMAdapter;
import com.wandrell.util.file.xml.module.adapter.XMLAdapter;
import com.wandrell.util.file.xml.module.interpreter.XMLDocumentInterpreter;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitAvailableWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>> {

    private final Map<String, Weapon> weapons;

    public GetAllUnitAvailableWeaponsCommand(final Map<String, Weapon> weapons) {
        super();

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() throws Exception {
        final FileParser<Map<String, Collection<Weapon>>> fileUnitWeapons;
        final XMLAdapter<Map<String, Collection<Weapon>>> adapter;
        final XMLDocumentInterpreter<Map<String, Collection<Weapon>>> reader;
        final XMLDocumentValidator validator;

        adapter = new JDOMAdapter<>();
        reader = new UnitAvailableWeaponsXMLDocumentReader(getWeapons());
        validator = new XSDValidator(
                ModelFileConf.UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitWeapons = new DefaultXMLFileParser<>(adapter, reader, validator);

        return fileUnitWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
