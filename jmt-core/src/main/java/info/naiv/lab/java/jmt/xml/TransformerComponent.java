package info.naiv.lab.java.jmt.xml;

import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.component.AbstractTlsServiceComponent;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 *
 * @author enlo
 */
public class TransformerComponent
        extends AbstractTlsServiceComponent<Transformer>
        implements SystemComponent {

    /**
     *
     * @param tag
     * @param provider
     * @return
     */
    @Override
    @Nonnull
    public Transformer getContent(Tag tag, ServiceProvider provider) {
        Transformer transformer = super.getContent(tag, provider);
        transformer.reset();
        return transformer;
    }

    @Override
    public Class<? extends Transformer> getContentType() {
        return Transformer.class;
    }

    /**
     *
     * @param map
     * @param tag
     * @param provider
     * @return
     */
    @Override
    protected boolean handleNotFound(Map<Tag, Transformer> map, Tag tag, ServiceProvider provider) {
        try {
            TransformerFactory factory = provider.resolveService(TransformerFactory.class, tag);
            assert factory != null;
            map.put(tag, factory.newTransformer());
            return true;
        }
        catch (TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
