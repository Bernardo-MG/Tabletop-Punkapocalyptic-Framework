package com.wandrell.tabletop.punkapocalyptic.report.expression;

import java.util.Collection;
import java.util.LinkedList;

import net.sf.dynamicreports.report.builder.expression.AbstractSubDatasourceExpression;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public final class CurrentObjectDatasourceExpression extends
        AbstractSubDatasourceExpression<Object> {

    private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

    public CurrentObjectDatasourceExpression(
            DRIExpression<? extends Object> expression) {
        super(expression);
    }

    public CurrentObjectDatasourceExpression(String fieldName) {
        super(fieldName);
    }

    @Override
    protected JRDataSource createSubDatasource(Object data) {
        final Collection<Object> list;
        list = new LinkedList<>();
        list.add(data);
        return new JRBeanCollectionDataSource(list);
    }

}
