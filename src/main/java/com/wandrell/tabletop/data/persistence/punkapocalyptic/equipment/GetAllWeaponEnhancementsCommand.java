package com.wandrell.tabletop.data.persistence.punkapocalyptic.equipment;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.WeaponEnhancement;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.WeaponEnhancementParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllWeaponEnhancementsCommand implements
        ReturnCommand<Map<String, WeaponEnhancement>> {

    public GetAllWeaponEnhancementsCommand() {
        super();
    }

    @Override
    public final Map<String, WeaponEnhancement> execute() throws Exception {
        final ObjectParser<Map<String, WeaponEnhancement>> fileWeaponEnhancements;
        final ParserInterpreter<Map<String, WeaponEnhancement>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new WeaponEnhancementParserInterpreter();
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_WEAPON_ENHANCEMENT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_WEAPON_ENHANCEMENT));

        fileWeaponEnhancements = ParserFactory.getInstance().getJDOMXMLParser(
                reader, validator);

        return fileWeaponEnhancements.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.WEAPON_ENHANCEMENT));
    }

}
