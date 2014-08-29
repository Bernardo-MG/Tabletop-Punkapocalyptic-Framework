package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.unit;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.wandrell.punkapocalyptic.framework.api.dao.ArmorDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.ArmorDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.UnitArmorsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.PathUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllUnitArmorsCommand implements
        ReturnCommand<Map<String, Collection<Armor>>>, ArmorDAOAware {

    private ArmorDAO daoArmor;

    public GetAllUnitArmorsCommand() {
        super();
    }

    @Override
    public final Map<String, Collection<Armor>> execute() {
        final FileHandler<Map<String, Collection<Collection<Object>>>> fileUnitArmors;
        final Map<String, Collection<Collection<Object>>> unitArmorData;
        final Map<String, Collection<Armor>> unitArmors;
        Collection<Armor> armors;
        Iterator<Object> itr;

        fileUnitArmors = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Collection<Collection<Object>>>>(),
                new UnitArmorsXMLDocumentReader(),
                new XSDValidator(
                        PathUtils
                                .getClassPathResource(ModelFile.VALIDATION_UNIT_AVAILABILITY)));

        unitArmorData = fileUnitArmors.read(PathUtils
                .getClassPathResource(ModelFile.UNIT_AVAILABILITY));

        unitArmors = new LinkedHashMap<>();
        for (final Entry<String, Collection<Collection<Object>>> entry : unitArmorData
                .entrySet()) {
            armors = new LinkedList<>();
            for (final Collection<Object> data : entry.getValue()) {
                itr = data.iterator();
                armors.add(getArmorDAO().getArmor((String) itr.next(),
                        (Integer) itr.next()));
            }
            unitArmors.put(entry.getKey(), armors);
        }

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
