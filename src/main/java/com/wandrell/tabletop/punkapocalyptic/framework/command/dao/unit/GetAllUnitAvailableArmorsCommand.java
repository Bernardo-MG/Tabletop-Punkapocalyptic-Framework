package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.ArmorDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.ArmorDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitAvailableArmorsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

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
