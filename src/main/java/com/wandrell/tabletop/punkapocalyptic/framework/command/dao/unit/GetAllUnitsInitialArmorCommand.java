package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.ArmorDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.ArmorDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitInitialArmorXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.PathUtils;
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
                        PathUtils
                                .getClassPathResource(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitArmors = fileUnitArmors.read(PathUtils
                .getClassPathResource(ModelFile.UNIT_AVAILABILITY));

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
