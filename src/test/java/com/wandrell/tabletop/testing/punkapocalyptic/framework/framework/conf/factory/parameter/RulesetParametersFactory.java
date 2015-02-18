package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.jdom2.Document;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.parser.xml.XMLFileParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.RulesetParametersConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.MaxAllowedUnitsDocumentParser;
import com.wandrell.util.ResourceUtils;

public final class RulesetParametersFactory {

    private static final RulesetParametersFactory instance            = new RulesetParametersFactory();
    private static Object                         lockMaxAllowedUnits = new Object();
    private static Collection<Collection<Object>> valuesMaxUnits;

    public static final RulesetParametersFactory getInstance() {
        return instance;
    }

    private static final Iterator<Object[]> getParameters(
            final Collection<Collection<Object>> valuesTable) {
        final Collection<Object[]> result;

        result = new LinkedList<>();
        for (final Collection<Object> values : valuesTable) {
            result.add(values.toArray());
        }

        return result.iterator();
    }

    private RulesetParametersFactory() {
        super();
    }

    public final Iterator<Object[]> getMaxAllowedUnitsValues() throws Exception {
        final Parser<Reader, Document> parserFile;
        final Parser<Document, Collection<Collection<Object>>> parserParams;

        if (valuesMaxUnits == null) {
            synchronized (lockMaxAllowedUnits) {
                if (valuesMaxUnits == null) {
                    parserFile = new XMLFileParser();
                    parserParams = new MaxAllowedUnitsDocumentParser();

                    valuesMaxUnits = parserParams
                            .parse(parserFile.parse(ResourceUtils
                                    .getClassPathReader(RulesetParametersConf.PROPERTIES_MAX_ALLOWED_UNITS)));
                }
            }
        }

        return getParameters(valuesMaxUnits);
    }

}
