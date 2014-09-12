package com.wandrell.tabletop.data.persistence.punkapocalyptic.armor.command;

import java.util.Map;

import com.wandrell.tabletop.business.conf.punkapocalyptic.ModelFileConf;
import com.wandrell.tabletop.business.model.punkapocalyptic.inventory.Armor;
import com.wandrell.tabletop.business.util.file.punkapocalyptic.equipment.ArmorsXMLDocumentReader;
import com.wandrell.tabletop.business.util.tag.punkapocalyptic.dao.RulesetDAOAware;
import com.wandrell.tabletop.data.persistence.punkapocalyptic.RulesetDAO;
import com.wandrell.util.ResourceUtils;
import com.wandrell.util.command.ReturnCommand;
import com.wandrell.util.file.FileHandler;
import com.wandrell.util.file.xml.DefaultXMLFileHandler;
import com.wandrell.util.file.xml.module.reader.XMLDocumentReader;
import com.wandrell.util.file.xml.module.validator.XMLDocumentValidator;
import com.wandrell.util.file.xml.module.validator.XSDValidator;

public final class GetAllArmorsCommand implements
        ReturnCommand<Map<String, Armor>>, RulesetDAOAware {

    private RulesetDAO daoRule;

    public GetAllArmorsCommand() {
        super();
    }

    @Override
    public final Map<String, Armor> execute() {
        final FileHandler<Map<String, Armor>> fileArmors;
        final XMLDocumentReader<Map<String, Armor>> reader;
        final XMLDocumentValidator validator;

        reader = new ArmorsXMLDocumentReader(getSpecialRuleDAO());
        validator = new XSDValidator(
                ModelFileConf.VALIDATION_ARMOR,
                ResourceUtils
                        .getClassPathInputStream(ModelFileConf.VALIDATION_ARMOR));

        fileArmors = new DefaultXMLFileHandler<>(reader, validator);

        return fileArmors.read(ResourceUtils
                .getClassPathInputStream(ModelFileConf.ARMOR));
    }

    @Override
    public final void setRulesetDAO(final RulesetDAO dao) {
        daoRule = dao;
    }

    protected final RulesetDAO getSpecialRuleDAO() {
        return daoRule;
    }

}
