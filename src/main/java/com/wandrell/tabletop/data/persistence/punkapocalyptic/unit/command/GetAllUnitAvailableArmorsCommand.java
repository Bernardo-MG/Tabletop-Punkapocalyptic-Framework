package com.wandrell.tabletop.data.persistence.punkapocalyptic.unit.command;

import java.util.Collection;
import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.unit.UnitAvailableArmorsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.ArmorDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.ArmorDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.validator.XSDValidator;
import com.wandrell.util.file.xml.module.writer.DisabledXMLWriter;

public final class GetAllUnitAvailableArmorsCommand implements
        ReturnCommand<Map<String, Collection<Armor>>>, ArmorDAOAware {

    private ArmorDAO daoArmor;

    public GetAllUnitAvailableArmorsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<Armor>> execute() {
        final FileHandler<Map<String, Collection<Armor>>> fileUnitArmors;
        final Map<String, Collection<Armor>> unitArmors;

        fileUnitArmors = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<Armor>>>(),
                new UnitAvailableArmorsXMLDocumentReader(getArmorDAO()),
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

    protected ArmorDAO getArmorDAO() {
        return daoArmor;
    }

}
