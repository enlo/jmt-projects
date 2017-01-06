package info.naiv.lab.java.jmt.template.mvel;

import info.naiv.lab.java.jmt.ExtendProperties;
import static info.naiv.lab.java.jmt.Misc.isBlank;
import info.naiv.lab.java.jmt.collection.Lookup;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

/**
 *
 * @author enlo
 */
public class MvelExpressiveProperties extends ExtendProperties {

    private static final long serialVersionUID = -2208239490931819164L;

    private final LookupVariableResolverFactory factory;
    private final Map<String, CompiledTemplate> templates = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public MvelExpressiveProperties() {
        this.factory = new LookupVariableResolverFactory(new PropertiesLookup(this));
    }

    /**
     * Constructor.
     *
     * @param defaults
     */
    public MvelExpressiveProperties(Properties defaults) {
        super(defaults);
        this.factory = new LookupVariableResolverFactory(new PropertiesLookup(this));
    }

    @Override
    public synchronized void clear() {
        super.clear();
        templates.clear();
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public synchronized MvelExpressiveProperties clone() {
        return (MvelExpressiveProperties) super.clone();
    }

    /**
     * Placeholder を実際の値に変換し、戻す.
     *
     * @param value 値
     * @return Placeholder 評価後の値
     */
    @Override
    protected String eval(String value) {
        if (isBlank(value)) {
            return value;
        }
        CompiledTemplate template = templates.get(value);
        if (template == null) {
            template = TemplateCompiler.compileTemplate(value);
            templates.put(value, template);
        }
        return (String) TemplateRuntime.execute(template, null, factory);
    }

    private static class PropertiesLookup implements Lookup<String, String>, Serializable {

        private static final long serialVersionUID = 1L;

        private final MvelExpressiveProperties props;

        PropertiesLookup(MvelExpressiveProperties props) {
            this.props = props;
        }

        @Override
        public boolean containsKey(String key) {
            return true;
        }

        @Override
        public String get(String key) {
            String value = props.getProperty(key);
            if (value == null) {
                value = System.getProperty(key);
                if (value == null) {
                    value = System.getenv(key);
                }
            }
            return value;
        }

    }

}
