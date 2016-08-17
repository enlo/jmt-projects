/*
 * The MIT License
 *
 * Copyright 2016 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.jdbc.sql.template.config;

import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.Misc.isNotBlank;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplate;
import info.naiv.lab.java.jmt.jdbc.sql.template.SqlTemplateLoader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 *
 * @author enlo
 */
@Slf4j
public class SqlTemplateInjector implements BeanPostProcessor, BeanFactoryAware {

    BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        doWithFields(bean.getClass(), new Callback(bean));
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     *
     * @param anno
     * @param categoryAnno
     * @return
     */
    protected String getCategory(InjectSql anno, SqlTemplateCategoryOf categoryAnno) {
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
     * @throws BeansException
     */
    protected SqlTemplateLoader getLoaderFromBeanFactory(String loaderName) throws BeansException {
        if (beanFactory == null) {
            logger.debug("no bean factory.");
            return null;
        }
        try {
            if (isBlank(loaderName)) {
                logger.debug("getBean(SqlTemplateLoader.class)");
                return beanFactory.getBean(SqlTemplateLoader.class);
            }
            else {
                logger.debug("getBean('{}', SqlTemplateLoader.class)", loaderName);
                return beanFactory.getBean(loaderName, SqlTemplateLoader.class);
            }
        }
        catch (BeansException ex) {
            logger.debug("SqlTemplateLoader not found.", ex);
            return null;
        }
    }

    /**
     *
     * @param loaderName
     * @return
     * @throws BeansException
     */
    protected SqlTemplateLoader getLoaderFromServiceProvider(String loaderName) throws BeansException {
        if (isBlank(loaderName)) {
            return ServiceProviders.resolveService(SqlTemplateLoader.class);
        }
        else {
            return ServiceProviders.resolveService(SqlTemplateLoader.class, Tag.of(loaderName));
        }
    }

    /**
     *
     * @param anno
     * @param field
     * @return
     */
    protected String getName(InjectSql anno, Field field) {
        String name = anno.name();
        if (isBlank(name)) {
            name = field.getName();
        }
        return name;
    }

    /**
     *
     * @param anno
     * @return
     */
    protected SqlTemplateLoader getSqlTemplateLoader(InjectSql anno) {
        String loaderName = anno.loader();
        SqlTemplateLoader loader;
        loader = getLoaderFromBeanFactory(loaderName);
        if (loader == null) {
            loader = getLoaderFromServiceProvider(loaderName);
        }
        return loader;
    }

    /**
     *
     * @param anno
     * @param categoryAnno
     * @param field
     * @return
     */
    protected SqlTemplate getTemplate(InjectSql anno, SqlTemplateCategoryOf categoryAnno, Field field) {
        SqlTemplateLoader templ = getSqlTemplateLoader(anno);
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

    private class Callback implements FieldCallback {

        final Object bean;
        final SqlTemplateCategoryOf category;

        Callback(Object bean) {
            this.bean = bean;
            this.category = AnnotationUtils.findAnnotation(bean.getClass(), SqlTemplateCategoryOf.class);
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            makeAccessible(field);
            InjectSql anno = field.getAnnotation(InjectSql.class);
            if (anno != null) {
                SqlTemplate templ = getTemplate(anno, category, field);
                field.set(bean, templ);
            }
        }

    }
}
