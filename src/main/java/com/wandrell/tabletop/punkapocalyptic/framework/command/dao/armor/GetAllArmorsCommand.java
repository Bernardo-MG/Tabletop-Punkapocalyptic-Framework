package com.wandrell.tabletop.punkapocalyptic.framework.command.dao.armor;

import java.util.Map;

import com.wandrell.punkapocalyptic.framework.api.dao.SpecialRuleDAO;
import com.wandrell.tabletop.punkapocalyptic.framework.conf.ModelFile;
import com.wandrell.tabletop.punkapocalyptic.framework.tag.dao.SpecialRuleDAOAware;
import com.wandrell.tabletop.punkapocalyptic.framework.util.file.ArmorsXMLDocumentReader;
import com.wandrell.tabletop.punkapocalyptic.inventory.Armor;
import com.wandrell.util.PathUtils;
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
                new XSDValidator(PathUtils
                        .getClassPathResource(ModelFile.VALIDATION_ARMOR)));

        armors = fileArmors.read(PathUtils
                .getClassPathResource(ModelFile.ARMOR));

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
