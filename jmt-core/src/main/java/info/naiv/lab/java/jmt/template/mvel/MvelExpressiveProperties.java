package info.naiv.lab.java.jmt.template.mvel;

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
public class MvelExpressiveProperties extends Properties {

    private final LookupVariableResolverFactory factory;
    private final Map<String, CompiledTemplate> templates = new ConcurrentHashMap<>();
    private final Lookup thisLookup;

    public MvelExpressiveProperties() {
        this.thisLookup = new PropertiesLookup(this);
        this.factory = new LookupVariableResolverFactory(thisLookup);
    }

    public MvelExpressiveProperties(Properties defaults) {
        super(defaults);
        this.thisLookup = new PropertiesLookup(this);
        this.factory = new LookupVariableResolverFactory(thisLookup);
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
     * プロパティの内容を固定化したものを戻す.
     *
     * @return 固定したプロパティ.
     */
    public Properties fix() {
        Properties props = new Properties();
        for (String key : this.stringPropertyNames()) {
            String value = this.getProperty(key);
            if (value != null) {
                props.setProperty(key, value);
            }
        }
        return props;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return eval(super.getProperty(key, defaultValue));
    }

    @Override
    public String getProperty(String key) {
        return eval(super.getProperty(key));
    }

    /**
     * プレイスホルダーを実際の値に変換し、戻す.
     *
     * @param value 値
     * @return プレイスホルダー評価後の値
     */
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

    /**
     * プロパティを取得し、プレイスホルダーを評価する.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected final String getPropertyCore(String key, String defaultValue) {
        String value = super.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private static class PropertiesLookup implements Lookup<String, String>, Serializable {

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
