package com.wandrell.tabletop.util.command.punkapocalyptic.dao.armor;

import java.util.Map;

import com.wandrell.tabletop.conf.punkapocalyptic.ModelFile;
import com.wandrell.tabletop.data.dao.punkapocalyptic.SpecialRuleDAO;
import com.wandrell.tabletop.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.util.file.punkapocalyptic.equipment.ArmorsXMLDocumentReader;
import com.wandrell.tabletop.util.tag.punkapocalyptic.dao.SpecialRuleDAOAware;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.api.FileHandler;
import com.wandrell.util.file.impl.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.impl.xml.DisabledXMLWriter;
import com.wandrell.util.file.impl.xml.XSDValidator;

public final class GetAllArmorsCommand implements
        ReturnCommand<Map<String, Armor>>, SpecialRuleDAOAware {

    private SpecialRuleDAO daoRule;

    public GetAllArmorsCommand() {
        super();
    }

    @Override
    public final Map<String, Armor> execute() {
        final FileHandler<Map<String, Armor>> fileArmors;
        final Map<String, Armor> armors;

        fileArmors = new DefaultXMLFileHandler<>(
                new DisabledXMLWriter<Map<String, Armor>>(),
                new ArmorsXMLDocumentReader(getSpecialRuleDAO()),
                new XSDValidator(ModelFile.VALIDATION_ARMOR, ResourceUtils
                        .getClassPathInputStream(ModelFile.VALIDATION_ARMOR)));

        armors = fileArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFile.ARMOR));

        return armors;
    }

    @Override
    public final void setSpecialRuleDAO(final SpecialRuleDAO dao) {
        daoRule = dao;
    }

    protected final SpecialRuleDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
