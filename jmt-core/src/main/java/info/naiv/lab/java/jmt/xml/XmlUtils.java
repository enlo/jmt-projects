package info.naiv.lab.java.jmt.xml;

import info.naiv.lab.java.jmt.annotation.Nonempty;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.resolveService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import static javax.xml.xpath.XPathConstants.NODESET;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import lombok.NonNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author enlo
 */
public abstract class XmlUtils {

    /**
     *
     * @param path
     * @return
     * @throws XPathExpressionException
     */
    @Nonnull
    public static XPathExpression getXPath(@Nonempty String path) throws XPathExpressionException {
        XPath xpath = get(XPath.class);
        return xpath.compile(path);
    }

    /**
     *
     * @param source
     * @param path
     * @param newValue
     * @return
     * @throws XPathExpressionException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    @Nonnull
    public static Document setNodeValue(@NonNull Document source, String path, String newValue) throws XPathExpressionException, IOException, SAXException {
        XPath xpath = get(XPath.class);
        Object obj = xpath.evaluate(path, source, NODESET);
        NodeList nl = (NodeList) obj;
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            n.setNodeValue(newValue);
        }
        return source;
    }

    /**
     *
     * @param source
     * @return
     * @throws TransformerException
     */
    @Nonnull
    public static byte[] toByteArray(@NonNull DOMSource source) throws TransformerException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Result result = new StreamResult(os);
        Transformer transformer = get(Transformer.class);
        transformer.transform(source, result);
        return os.toByteArray();
    }

    /**
     *
     * @param source
     * @return
     * @throws TransformerException
     */
    @Nonnull
    public static byte[] toByteArray(@NonNull Document source) throws TransformerException {
        return toByteArray(new DOMSource(source));
    }

    /**
     *
     * @param source
     * @return
     * @throws IOException
     * @throws SAXException
     */
    @Nonnull
    public static Document toDocument(@NonNull InputSource source) throws IOException, SAXException {
        DocumentBuilder builder = get(DocumentBuilder.class);
        return builder.parse(source);
    }

    /**
     *
     * @param source
     * @return
     * @throws TransformerException
     */
    @Nonnull
    public static String toString(@NonNull DOMSource source) throws TransformerException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        Transformer transformer = get(Transformer.class);
        transformer.transform(source, result);
        return sw.toString();
    }

    static <T> T get(Class<T> clazz) {
        return requireNonNull(resolveService(clazz));
    }

    private XmlUtils() {
    }
}
