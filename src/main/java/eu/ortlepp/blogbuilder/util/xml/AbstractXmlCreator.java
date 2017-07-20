package eu.ortlepp.blogbuilder.util.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Abstract class to create XML documents / files. Contains some useful helper-methods.
 *
 * @author Thorsten Ortlepp
 */
public abstract class AbstractXmlCreator {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(AbstractXmlCreator.class.getName());

    /** The XML document. */
    protected final Document xmlDocument;

    /** The root element of the XML document. */
    protected Element xmlRoot;


    /**
     * Constructor, initializes the XML document.
     */
    public AbstractXmlCreator() {
        try {
            final DocumentBuilderFactory xmlFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder xmlBuilder = xmlFactory.newDocumentBuilder();
            xmlDocument = xmlBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            LOGGER.severe("Initialization of the XML builder failed");
            throw new RuntimeException(ex);
        }
    }


    /**
     * Initialize the root element of the XML document. The initialization depends on the XML file to create.
     */
    protected abstract void initRootElement();


    /**
     * Create a XML element with text content.
     *
     * @param name The name of the element
     * @param content The content of the element
     * @return The created element
     */
    protected Element createElement(final String name, final String content) {
        final Element element = xmlDocument.createElement(name);
        element.appendChild(xmlDocument.createTextNode(content));
        return element;
    }


    /**
     * Create an XML attribute with a specific value.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     * @return The created attribute
     */
    protected Attr createAttribute(final String name, final String value) {
        final Attr attr = xmlDocument.createAttribute(name);
        attr.setValue(value);
        return attr;
    }


    /**
     * Write the created XML document to a physical XML file.
     *
     * @param file The XML file to write
     */
    protected void writeXmlFile(final File file) {
        try {
            final TransformerFactory xmlTransFactory = TransformerFactory.newInstance();
            final Transformer transformer = xmlTransFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xmlDocument.setXmlStandalone(true);
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(file));
            LOGGER.info(String.format("%s created", file.getName()));
        } catch (TransformerException ex) {
            LOGGER.severe(String.format("Writing %s failed: %s", file.getName(), ex.getMessage()));
        }
    }

}
