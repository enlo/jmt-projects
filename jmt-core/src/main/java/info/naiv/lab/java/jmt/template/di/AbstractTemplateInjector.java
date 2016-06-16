package info.naiv.lab.java.jmt.template.di;

import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.TemplateLoader;
import info.naiv.lab.java.jmt.template.annotation.InjectTemplate;
import info.naiv.lab.java.jmt.template.annotation.TemplateCategoryOf;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractTemplateInjector {

    /**
     *
     * @param anno
     * @param categoryAnno
     * @return
     */
    protected String getCategory(InjectTemplate anno, TemplateCategoryOf categoryAnno) {
        String category = anno.category();
        if (isBlank(category) && categoryAnno != null) {
            category = categoryAnno.value();
        }
        return category;
    }

    /**
     *
     * @param loaderName
     * @return
     */
    protected TemplateLoader getLoaderFromServiceProvider(String loaderName) {
        if (isBlank(loaderName)) {
            return ServiceProviders.resolveService(TemplateLoader.class);
        }
        else {
            return ServiceProviders.resolveService(TemplateLoader.class, Tag.of(loaderName));
        }
    }

    /**
     *
     * @param anno
     * @param field
     * @return
     */
    protected String getName(InjectTemplate anno, Field field) {
        String name = anno.name();
        if (isBlank(name)) {
            name = field.getName();
        }
        return name;
    }

    /**
     *
     * @param anno
     * @param categoryAnno
     * @param field
     * @return
     */
    protected Template getTemplate(InjectTemplate anno, TemplateCategoryOf categoryAnno, Field field) {
        TemplateLoader templ = getTemplateLoader(anno);
        if (templ != null) {
            String category = getCategory(anno, categoryAnno);
            String name = getName(anno, field);
            String charset = anno.charset();
            if (isNotBlank(charset) && Charset.isSupported(charset)) {
                logger.debug("load(category={}, name={}, charset={})", category, name, charset);
                return templ.load(category, name, Charset.forName(charset));
            }
            else {
                logger.debug("load(category={}, name={})", category, name);
                return templ.load(category, name);
            }
        }
        else {
            logger.debug("no template loader.");
            return null;
        }
    }

    /**
     *
     * @param anno
     * @return
     */
    protected TemplateLoader getTemplateLoader(InjectTemplate anno) {
        String loaderName = anno.loader();
        TemplateLoader loader;
        loader = getTemplateLoaderCore(loaderName);
        if (loader == null) {
            loader = getLoaderFromServiceProvider(loaderName);
        }
        return loader;
    }

    /**
     *
     * @param loaderName
     * @return
     */
    protected abstract TemplateLoader getTemplateLoaderCore(String loaderName);

}
