/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.infrastructure.di;

import static info.naiv.lab.java.jmt.Misc.isNotEmpty;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.annotation.InjectService;
import info.naiv.lab.java.jmt.infrastructure.annotation.TagOf;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static java.util.Arrays.asList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils.FieldCallback;
import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

/**
 *
 * @author enlo
 */
@Slf4j
public class SpringServiceInjector implements BeanPostProcessor, BeanFactoryAware {

    BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object o, String string) throws BeansException {
        return o;
    }

    @Override
    @ReturnNonNull
    public Object postProcessBeforeInitialization(final Object o, final String string) throws BeansException {
        doWithFields(o.getClass(), new Callback(o));
        return o;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        beanFactory = bf;
    }

    Object getService(InjectService anno, Annotation[] annotations, Class<?> clazz) throws IllegalAccessException, IllegalArgumentException {
        try {
            Object obj;
            Tag tag = ServiceInjectionUtils.findTag(anno, asList(annotations));
            if (tag == null) {
                logger.debug("resolveService(class={})", clazz);
                obj = getServiceProvider().resolveService(clazz);
            }
            else {
                logger.debug("resolveService(class={}, tag={})", clazz, tag);
                obj = getServiceProvider().resolveService(clazz, tag);
            }
            return obj;
        }
        catch (NoSuchMethodException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @ReturnNonNull
    ServiceProvider getServiceProvider() {
        ServiceProvider sp = null;
        try {
            if (beanFactory != null) {
                sp = beanFactory.getBean(ServiceProvider.class);
                logger.debug("use Bean.");
            }
        }
        catch (BeansException ex) {
            logger.debug("ServiceProvider not found.", ex);
            sp = null;
        }
        if (sp == null) {
            sp = getThreadContainer();
            logger.debug("use ThreadContainer.");
        }
        return sp;
    }

    private class Callback implements FieldCallback {

        final Object o;

        Callback(Object o) {
            this.o = o;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            makeAccessible(field);
            InjectService anno = field.getAnnotation(InjectService.class);
            if (anno != null) {
                Class<?> clazz = field.getType();
                Annotation[] annotations = field.getDeclaredAnnotations();
                Object obj = getService(anno, annotations, clazz);
                field.set(o, obj);
            }
        }

    }
}
