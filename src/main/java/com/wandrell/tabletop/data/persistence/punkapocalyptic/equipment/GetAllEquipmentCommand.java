package com.wandrell.tabletop.data.persistence.punkapocalyptic.equipment;

import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Equipment;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.EquipmentParserInterpreter;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.parser.ObjectParser;
import com.wandrell.util.parser.ParserFactory;
import com.wandrell.util.parser.module.interpreter.ParserInterpreter;
import com.wandrell.util.parser.module.validator.JDOMXSDValidator;
import com.wandrell.util.parser.module.validator.ParserValidator;

public final class GetAllEquipmentCommand implements
        ReturnCommand<Map<String, Equipment>> {

    public GetAllEquipmentCommand() {
        super();
    }

    @Override
    public final Map<String, Equipment> execute() throws Exception {
        final ObjectParser<Map<String, Equipment>> fileEquipment;
        final ParserInterpreter<Map<String, Equipment>, Document> reader;
        final ParserValidator<Document, SAXBuilder> validator;

        reader = new EquipmentParserInterpreter();
        validator = new JDOMXSDValidator(
                ModelFileConf.VALIDATION_EQUIPMENT,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_EQUIPMENT));

        fileEquipment = ParserFactory.getInstance().getJDOMXMLParser(reader,
                validator);

        return fileEquipment.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.EQUIPMENT));
    }

}
