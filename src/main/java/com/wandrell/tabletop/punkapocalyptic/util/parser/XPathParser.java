package com.wandrell.tabletop.punkapocalyptic.util.parser;

import java.util.Collection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.wandrell.pattern.parser.Parser;

public final class XPathParser implements Parser<Document, Document> {

    private XPathExpression<Element> xpath;

    public XPathParser() {
        super();
    }

    @Override
    public final Document parse(final Document input) {
        final Collection<Element> nodes;
        final Element root;     // Root combining all the roots

        nodes = getXPathExpression().evaluate(input);

        root = new Element(input.getRootElement().getName());

        for (final Element node : nodes) {
            root.addContent(node.clone());
        }

        return new Document(root);
    }

    public final void setExpression(final String expression) {
        xpath = XPathFactory.instance().compile(expression, Filters.element());
    }

    private final XPathExpression<Element> getXPathExpression() {
        return xpath;
    }

}
