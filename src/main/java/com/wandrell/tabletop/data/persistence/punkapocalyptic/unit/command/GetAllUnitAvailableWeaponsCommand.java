package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Weapon;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableWeaponsXMLDocumentReader;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserUtils;
import com.wandrell.util.parser.xml.module.interpreter.JDOMXMLInterpreter;
import com.wandrell.util.parser.xml.module.validator.JDOMXMLValidator;
import com.wandrell.util.parser.xml.module.validator.XSDValidator;

public final class GetAllUnitAvailableWeaponsCommand implements
        ReturnCommand<Map<String, Collection<Weapon>>> {

    private final Map<String, Weapon> weapons;

    public GetAllUnitAvailableWeaponsCommand(final Map<String, Weapon> weapons) {
        super();

        this.weapons = weapons;
    }

    @Override
    public final Map<String, Collection<Weapon>> execute() throws Exception {
        final ObjectParser<Map<String, Collection<Weapon>>> fileUnitWeapons;
        final JDOMXMLInterpreter<Map<String, Collection<Weapon>>> reader;
        final JDOMXMLValidator validator;

        reader = new UnitAvailableWeaponsXMLDocumentReader(getWeapons());
        validator = new XSDValidator(
                ModelFileConf.UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitWeapons = ParserUtils.getJDOMXMLParser(reader, validator);

        return fileUnitWeapons.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    private final Map<String, Weapon> getWeapons() {
        return weapons;
    }

}
