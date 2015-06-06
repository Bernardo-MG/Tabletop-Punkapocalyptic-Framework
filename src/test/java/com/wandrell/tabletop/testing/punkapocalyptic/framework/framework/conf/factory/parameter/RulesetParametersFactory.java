package com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.factory.parameter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.jdom2.Document;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.parser.xml.XMLFileParser;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.conf.RulesetParametersConf;
import com.wandrell.tabletop.testing.punkapocalyptic.framework.framework.util.parser.MaxAllowedUnitsDocumentParser;

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
                            .parse(parserFile
                                    .parse(getClassPathReader(RulesetParametersConf.MAX_ALLOWED_UNITS)));
                }
            }
        }

        return getParameters(valuesMaxUnits);
    }

    /**
     * Creates an {@code InputStream} pointing to the file specified by the
     * path, if it exists.
     * <p>
     * If any problem occurs during this process a {@code RuntimeException} is
     * thrown, or an {@code IllegalArgumentException} if the file is not found.
     * 
     * @param path
     *            the path to transform
     * @return an {@code InputStream} pointing to the path
     */
    private final InputStream getClassPathInputStream(final String path) {
        final URL url;                  // URL parsed from the path
        final InputStream stream;       // Stream for the file

        url = getClassPathURL(path);

        checkArgument(url != null,
                String.format("The path %s is invalid", path));

        try {
            stream = new BufferedInputStream(url.openStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return stream;
    }

    /**
     * Creates a {@code Reader} pointing to the file specified by the path, if
     * it exists.
     * <p>
     * If any problem occurs during this process a {@code RuntimeException} is
     * thrown, or an {@code IllegalArgumentException} if the file is not found.
     * 
     * @param path
     *            the path to transform
     * @return an {@code InputStream} pointing to the path
     */
    private final Reader getClassPathReader(final String path) {
        return new BufferedReader(new InputStreamReader(
                getClassPathInputStream(path)));
    }

    /**
     * Creates an {@code URL} pointing to the file specified by the path, if it
     * exists.
     * 
     * @param path
     *            the path to transform
     * @return an URL pointing inside the class path
     */
    private final URL getClassPathURL(final String path) {
        checkNotNull(path, "Received a null pointer as path");

        return this.getClass().getClassLoader().getResource(path);
    }

}
