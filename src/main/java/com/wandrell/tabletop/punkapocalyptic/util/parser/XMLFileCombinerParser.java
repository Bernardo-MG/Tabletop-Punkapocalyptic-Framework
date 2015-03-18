package com.wandrell.tabletop.punkapocalyptic.util.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Reader;
import java.util.Collection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.wandrell.pattern.parser.Parser;
import com.wandrell.pattern.parser.xml.NotValidatedXMLFileParser;

public final class XMLFileCombinerParser implements
        Parser<Collection<Reader>, Document> {

    /**
     * Name for the root node.
     */
    private static final String ROOT_NAME = "root";

    public XMLFileCombinerParser() {
        super();

        // TODO: Move this to another library
    }

    @Override
    public final Document parse(final Collection<Reader> input)
            throws JDOMException {

        checkNotNull(input, "Received a null pointer as sources");

        for (final Reader reader : input) {
            checkNotNull(reader,
                    "Received a null pointer as one of the sources");
        }

        final Element root;     // Root combining all the roots
        final NotValidatedXMLFileParser parser;      // Parser for the stream

        root = new Element(ROOT_NAME);

        parser = new NotValidatedXMLFileParser();

        // Parses each stream and adds it's root to the main root
        for (final Reader reader : input) {
            root.addContent(parser.parse(reader).getRootElement().clone());
        }

        return new Document(root);
    }

}
