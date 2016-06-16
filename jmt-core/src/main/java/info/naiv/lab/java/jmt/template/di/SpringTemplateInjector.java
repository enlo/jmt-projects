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
package info.naiv.lab.java.jmt.template.di;

import static info.naiv.lab.java.jmt.Misc.isBlank;
import info.naiv.lab.java.jmt.template.Template;
import info.naiv.lab.java.jmt.template.TemplateLoader;
import info.naiv.lab.java.jmt.template.annotation.InjectTemplate;
import info.naiv.lab.java.jmt.template.annotation.TemplateCategoryOf;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;
import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 *
 * @author enlo
 */
@Slf4j
public class SpringTemplateInjector extends AbstractTemplateInjector implements BeanPostProcessor, BeanFactoryAware {

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
     * @param loaderName
     * @return
     */
    @Override
    protected TemplateLoader getTemplateLoaderCore(String loaderName) {
        if (beanFactory == null) {
            return null;
        }
        try {
            if (isBlank(loaderName)) {
                logger.debug("getBean(TemplateLoader.class)");
                return beanFactory.getBean(TemplateLoader.class);
            }
            else {
                logger.debug("getBean('{}', TemplateLoader.class)", loaderName);
                return beanFactory.getBean(loaderName, TemplateLoader.class);
            }
        }
        catch (BeansException ex) {
            logger.debug("TemplateLoader not found.", ex);
            return null;
        }
    }

    private class Callback implements FieldCallback, MethodCallback {

        final Object bean;
        final TemplateCategoryOf category;

        Callback(Object bean) {
            this.bean = bean;
            this.category = AnnotationUtils.findAnnotation(bean.getClass(), TemplateCategoryOf.class);
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            makeAccessible(field);
            InjectTemplate anno = field.getAnnotation(InjectTemplate.class);
            if (anno != null) {
                Template templ = getTemplate(anno, category, field);
                field.set(bean, templ);
            }
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
