package info.naiv.lab.java.jmt.template.di;

import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.TemplateLoader;
import info.naiv.lab.java.jmt.template.annotation.InjectTemplate;
import info.naiv.lab.java.jmt.template.annotation.TemplateCategoryOf;
import java.lang.reflect.Member;
import java.nio.charset.Charset;
import javax.annotation.CheckForNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public abstract class AbstractTemplateInjector {

    @Getter
    @Setter
    protected TemplateLoader defaultTemplateLoader;

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
     * @param loaderClass
     * @param loaderName
     * @return
     */
    protected TemplateLoader getLoaderFromServiceProvider(String loaderName, Class<? extends TemplateLoader> loaderClass) {
        if (isBlank(loaderName)) {
            return ServiceProviders.resolveService(loaderClass);
        }
        else {
            return ServiceProviders.resolveService(loaderClass, Tag.of(loaderName));
        }
    }

    /**
     *
     * @param anno
     * @param member
     * @return
     */
    protected String getName(InjectTemplate anno, Member member) {
        String name = anno.name();
        if (isBlank(name)) {
            name = member.getName();
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
    protected Template getTemplate(InjectTemplate anno, TemplateCategoryOf categoryAnno, Member field) {
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
        loader = getTemplateLoaderCore(loaderName, anno.loaderClass());
        if (loader == null) {
            loader = getLoaderFromServiceProvider(loaderName, anno.loaderClass());
        }
        if (loader == null) {
            loader = getDefaultTemplateLoader();
        }
        return loader;
    }

    /**
     * TemplateLoader 読み込み.
     *
     * @param loaderName ローダー名
     * @param loaderClass ローダークラス
     * @return
     */
    @CheckForNull
    protected abstract TemplateLoader getTemplateLoaderCore(String loaderName, Class<? extends TemplateLoader> loaderClass);

}
