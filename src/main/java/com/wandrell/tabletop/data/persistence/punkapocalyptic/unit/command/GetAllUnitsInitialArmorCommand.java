package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitInitialArmorXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.ArmorDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.ArmorDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllUnitsInitialArmorCommand implements
        ReturnCommand<Map<String, Armor>>, ArmorDAOAware {

    private ArmorDAO daoArmor;

    public GetAllUnitsInitialArmorCommand() {
        super();
    }

    @Override
    public final Map<String, Armor> execute() {
        final FileHandler<Map<String, Armor>> fileUnitArmors;
        final XMLDocumentReader<Map<String, Armor>> reader;
        final XMLDocumentValidator validator;

        reader = new UnitInitialArmorXMLDocumentReader(getArmorDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_UNIT_AVAILABILITY,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_UNIT_AVAILABILITY));

        fileUnitArmors = new DefaultXMLFileHandler<>(reader, validator);

        return fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.UNIT_AVAILABILITY));
    }

    @Override
    public final void setArmorDAO(final ArmorDAO dao) {
        daoArmor = dao;
    }

    protected final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

}
