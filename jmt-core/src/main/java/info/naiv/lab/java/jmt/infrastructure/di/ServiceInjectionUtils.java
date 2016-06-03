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
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.resolveService;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.infrastructure.annotation.InjectService;
import info.naiv.lab.java.jmt.infrastructure.annotation.TagOf;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author enlo
 */
@Slf4j
public class ServiceInjectionUtils {

    /**
     *
     * @param anno
     * @param annotations
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Tag findTag(InjectService anno, Iterable<Annotation> annotations) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (isNotEmpty(anno.value())) {
            return Tag.of(anno.value());
        }
        Tag result = null;
        for (Annotation a : annotations) {
            TagOf tagOf = a.annotationType().getAnnotation(TagOf.class);
            if (tagOf != null) {
                Class<? extends Annotation> annoType = a.annotationType();
                Method method = annoType.getMethod(tagOf.value());
                if (Serializable.class.isAssignableFrom(method.getReturnType())) {
                    Serializable val = (Serializable) method.invoke(a);
                    if (val != null) {
                        if (annoType == InjectService.class) {
                            if (!"".equals(val)) {
                                result = Tag.of(val);
                            }
                        }
                        else {
                            return Tag.of(val);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @param ip
     * @return
     */
    public static <T> T getService(Class<T> clazz, InjectionPoint ip) {
        try {
            Annotated at = ip.getAnnotated();
            InjectService anno = at.getAnnotation(InjectService.class);
            Tag tag = findTag(anno, at.getAnnotations());
            if (tag == null) {
                logger.debug("resolveService(class={})", clazz);
                return resolveService(clazz);
            }
            else {
                logger.debug("resolveService(class={}, tag={})", clazz, tag);
                return resolveService(clazz, tag);
            }
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private ServiceInjectionUtils() {
    }
}
