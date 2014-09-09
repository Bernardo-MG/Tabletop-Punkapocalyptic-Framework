package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.data.dao.punkapocalyptic.ArmorDAO;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.util.file.punkapocalyptic.unit.UnitInitialArmorXMLDocumentReader;
import com.wandrell.tabletop.util.tag.punkapocalyptic.dao.ArmorDAOAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllUnitsInitialArmorCommand implements
        ReturnCommand<Map<String, Armor>>, ArmorDAOAware {

    private ArmorDAO daoArmor;

    public GetAllUnitsInitialArmorCommand() {
        super();
    }

    @Override
    public final Map<String, Armor> execute() {
        final FileHandler<Map<String, Armor>> fileUnitArmors;
        final Map<String, Armor> unitArmors;

        fileUnitArmors = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Armor>>(),
                new UnitInitialArmorXMLDocumentReader(getArmorDAO()),
                new XSDValidator(
                        ModelFile.VALIDATION_UNIT_AVAILABILITY,
                        ResourceUtils
                                .getClassPathInputStream(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitArmors = fileUnitArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFile.UNIT_AVAILABILITY));

        return unitArmors;
    }

    @Override
    public final void setArmorDAO(final ArmorDAO dao) {
        daoArmor = dao;
    }

    protected final ArmorDAO getArmorDAO() {
        return daoArmor;
    }

}
